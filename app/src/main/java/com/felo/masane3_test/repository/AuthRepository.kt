package com.felo.masane3_test.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.felo.masane3_test.data.models.UserModel
import com.felo.masane3_test.data.state.DataState
import com.felo.masane3_test.data.state.GenericResponse
import com.felo.masane3_test.data.state.Response
import com.felo.masane3_test.data.state.ResponseType
import com.felo.masane3_test.network.AbsentLiveData
import com.felo.masane3_test.network.ApiInterface
import com.felo.masane3_test.network.ApiSuccessResponse
import com.felo.masane3_test.network.GenericApiResponse
import com.felo.masane3_test.network.JobManager
import com.felo.masane3_test.network.NetworkBoundResource
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository @Inject constructor(val apiInterface: ApiInterface) : JobManager("AuthRepository")
{
    private val TAG = "AuthRepository"

    suspend fun login(
        phone: String,
        password: String,
    ): LiveData<DataState<UserModel>>
    {
        return object : NetworkBoundResource<GenericResponse<UserModel>, UserModel>(
            isNetworkAvailable = true,
            isNetworkRequest = true,
            shouldLoadFromCache = false,
            shouldCancelIfNoInternet = false
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse<UserModel>>) {
                Log.d(TAG, "Login handleApiSuccessResponse: $response")
                if (response.body.status) {
                    onCompleteJob(
                        DataState.success(
                            data = response.body.data,
                            response = Response(
                                message = "Jobs",
                                responseType = ResponseType.None()
                            )
                        )
                    )
                } else {
                    Log.d(TAG, "handleApiSuccessResponse: Return Error")
                    onCompleteJob(
                        DataState.error(
                            response = Response(
                                message = response.body.message,
                                responseType = ResponseType.Toast()
                            )
                        )
                    )
                }
            }

            override suspend fun createCacheRequest() {
                TODO("Not yet implemented")
            }

            override suspend fun createCall(): LiveData<GenericApiResponse<GenericResponse<UserModel>>> {
                Log.i("Felo", "create Call Api")
                return apiInterface.login(phone, password)
            }

            override fun setJob(job: Job) {
                addJop("login", job)
            }

            override fun loadFromCache(): LiveData<UserModel> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: UserModel?) {
            }

        }.asLiveData()
    }


}