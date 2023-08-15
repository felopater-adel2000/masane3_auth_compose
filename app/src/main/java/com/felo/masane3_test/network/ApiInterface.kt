package com.felo.masane3_test.network

import androidx.lifecycle.LiveData
import com.felo.masane3_test.data.models.PostModel
import com.felo.masane3_test.data.models.UserModel
import com.felo.masane3_test.data.state.GenericResponse
import com.felo.masane3_test.utils.Constants
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface
{

    @GET("v2/posts")
    fun getPosts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Constants.PAGINATION_PAGE_SIZE,
        @Query("search") search: String? = null,
        @Query("id") id: Int? = null,
        @Query("group_id") groupId: Int? = null,
        @Query("user_id") userId: Int? = null
    ): LiveData<GenericApiResponse<GenericResponse<List<PostModel>>>>

    @POST("v2/auth/user/login")
    @FormUrlEncoded
   suspend fun login(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): LiveData<GenericApiResponse<GenericResponse<UserModel>>>
}