package com.felo.masane3_test.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.felo.masane3_test.data.models.PostModel
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
import com.felo.masane3_test.utils.Constants
import kotlinx.coroutines.Job
import javax.inject.Inject

class PostsRepository @Inject constructor(val apiInterface: ApiInterface) : JobManager("postsReposittory")
{
    private val TAG = "PostsRepository"
    fun getPosts(
        page: Int = 0,
        limit: Int = Constants.PAGINATION_PAGE_SIZE,
        search: String? = null,
        id: Int? = null,
        groupId: Int? = null,
        userId: Int? = null
    ): LiveData<DataState<List<PostModel>>>
    {
        return object: NetworkBoundResource<GenericResponse<List<PostModel>>, List<PostModel>>(
            isNetworkAvailable = true,
            isNetworkRequest = true,
            shouldLoadFromCache = false,
            shouldCancelIfNoInternet = false,
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse<List<PostModel>>>) {
                Log.d(TAG, "handleApiSuccessResponse: ")
                if (response.body.status && response.body.data != null) {
                    val data = response.body.data
                    onCompleteJob(
                        DataState.success(
                            data = data,
                            response = Response(
                                message = "InfoNewsDetails",
                                responseType = ResponseType.None()
                            )
                        )
                    )
                } else {
                    Log.d(TAG, "handleApiSuccessResponse: ${response.body.message}")
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
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse<List<PostModel>>>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJop("getPosts", job)
            }

            override fun loadFromCache(): LiveData<List<PostModel>> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<PostModel>?) {
            }
        }.asLiveData()
    }
}