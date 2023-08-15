package com.felo.masane3_test.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.felo.masane3_test.data.state.DataState
import com.felo.masane3_test.data.state.GenericResponse
import com.felo.masane3_test.data.state.Response
import com.felo.masane3_test.data.state.ResponseType
import com.felo.masane3_test.utils.Constants.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.felo.masane3_test.utils.Constants.Companion.ERROR_UNKNOWN
import com.felo.masane3_test.utils.Constants.Companion.NETWORK_TIMEOUT
import com.felo.masane3_test.utils.Constants.Companion.TESTING_CACHE_DELAY
import com.felo.masane3_test.utils.Constants.Companion.TESTING_NETWORK_DELAY
import com.felo.masane3_test.utils.Constants.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import com.felo.masane3_test.utils.Constants.Companion.UNABLE_TO_RESOLVE_HOST
import com.felo.masane3_test.utils.MainUtils.Companion.isNetworkError
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject, CacheObject>(
    isNetworkAvailable: Boolean,//is there a network connection?
    isNetworkRequest: Boolean,//is this a network request?
    shouldLoadFromCache: Boolean,//should the cached data loaded firstly?
    shouldCancelIfNoInternet: Boolean,//should cancel if there is no internet
    val shouldUseToastOnErrorReturn: Boolean = true
) {
    private val TAG = "NetworkBoundResource"

    protected val result = MediatorLiveData<DataState<CacheObject>>()
    protected lateinit var job: CompletableJob
//    protected lateinit var GlobalScope: GlobalScope


    init {
        setJob(initNewJob())
        if (isNetworkRequest) setValue(DataState.loading(isLoading = true))

        if (shouldLoadFromCache) {
            val dbSource = loadFromCache()
            result.addSource(dbSource) {
                result.removeSource(dbSource)
                setValue(DataState.loading(isLoading = true, cashedData = it))
            }
        }

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                doNetworkRequest()
            } else {
                if (shouldCancelIfNoInternet) {
                    onErrorReturn(
                        UNABLE_TODO_OPERATION_WO_INTERNET,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                } else {
                    doCacheRequest()
                }
            }
        } else {
            doCacheRequest()
        }

    }

    private fun doCacheRequest() {
        GlobalScope.launch {
            //Fake delay
            delay(TESTING_CACHE_DELAY)
            //View data from cache and return
            createCacheRequest()
        }
    }

    private fun doNetworkRequest() {
        GlobalScope.launch {
            //simulatea network delay for testing
            delay(TESTING_NETWORK_DELAY)
            withContext(Dispatchers.Main) {
                //make network call
                val apiResponse = createCall()
                result.addSource(apiResponse) { response ->
                    result.removeSource(apiResponse)
                    GlobalScope.launch {
                        handleNetworkCall(response)
                    }
                }
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            delay(NETWORK_TIMEOUT)
            if (!job.isCompleted) {
                Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT.")
                job.cancel(CancellationException(UNABLE_TO_RESOLVE_HOST))
            }
        }
    }


    private suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>?) {
        when (response) {
            is ApiSuccessResponse -> {
                Log.d(TAG, "handleNetworkCall: Success Call Api")
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                Log.d(TAG, "handleNetworkCall: ApiResponseError")
                Log.e(TAG, "NetworkBoundResource: ${response.errorMessage}")
                //onErrorReturn(response.errorMessage, false, true)
                try {
                    val errorBody =
                        Gson().fromJson(response.errorMessage, GenericResponse::class.java)
                    if(errorBody.message != null)
                    {
                        onErrorReturn(errorBody.message, shouldUseDialog = false, shouldUseToast = true)
                    }
                    else if(errorBody.errors != null)
                    {
                        if(errorBody.errors?.isNotEmpty() == true)
                        {
                            onErrorReturn(errorBody.errors!![0], shouldUseDialog = false, shouldUseToast = true)
                        }
                    }
                    else
                    {
                        onErrorReturn(errorBody.message, shouldUseDialog = false, shouldUseToast = true)
                    }
                } catch (e: Exception) {
                    onErrorReturn(
                        response.errorMessage,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }
            }
            is ApiEmptyResponse -> {
                Log.e(TAG, "NetworkBoundResource: Request returned NOTHING (HTTP 204)")
                onErrorReturn("HTTP 204. Returned nothing.", true, false)
            }
            else -> {
                Log.d(TAG, "handleNetworkCall: else branch")
            }
        }
    }

    fun onCompleteJob(dataState: DataState<CacheObject>) {
        GlobalScope.launch(Dispatchers.Main) {
            job.complete()
            setValue(dataState)
        }
    }

    private fun setValue(dataState: DataState<CacheObject>) {
        result.value = dataState
    }

    fun onErrorReturn(errorMessage: String?, shouldUseDialog: Boolean, shouldUseToast: Boolean) {
        var msg = errorMessage
        var responseType: ResponseType = ResponseType.None()
        if (msg == null) {
            msg = ERROR_UNKNOWN
        } else if (isNetworkError(msg)) {
            msg = ERROR_CHECK_NETWORK_CONNECTION
        }

        if (shouldUseToast) responseType = ResponseType.Toast()
        if (shouldUseDialog) responseType = ResponseType.Dialog()

        // complete job and emit data state
        onCompleteJob(
            DataState.error(
                response = Response(
                    message = msg,
                    responseType = responseType
                )
            )
        )
    }

    private fun initNewJob(): Job {
        Log.d(TAG, "initNewJob: called")
        job = Job()
        job.invokeOnCompletion {
            Log.d(TAG, "initNewJob: invokeOnCompletion")
            if (job.isCancelled) 
            {
                Log.d(TAG, "initNewJob: job.isCancelled = ${job.isCancelled}")
                Log.d(TAG, "initNewJob: it == null")
                it?.let {
                    // show error dialog
                    onErrorReturn(it.message, shouldUseDialog = false, shouldUseToast = false)
                } ?: onErrorReturn(ERROR_UNKNOWN, shouldUseDialog = false, shouldUseToast = false)
            }
            else if (job.isCompleted) {
                Log.d(TAG, "initNewJob: job.isCompleted = ${job.isCompleted}")
                Log.d(TAG, "invoke: Job has been completed")
                //Do Nothing, should be handled already
            }
        }
//        GlobalScope = GlobalScope(Dispatchers.IO + job)
        Log.d(TAG, "initNewJob: return Job")
        return job
    }

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)
    abstract suspend fun createCacheRequest()
    abstract suspend fun createCall(): LiveData<GenericApiResponse<ResponseObject>>
    abstract fun setJob(job: Job)
    abstract fun loadFromCache(): LiveData<CacheObject>
    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)
    fun asLiveData() = result as LiveData<DataState<CacheObject>>
}