package com.felo.masane3_test.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(
    @SerializedName("id")
    @Expose
    val id: Int? = 0,

    @SerializedName("content")
    @Expose
    val content: String? = null,

    @SerializedName("group_id")
    @Expose
    val groupId: String? = null,

    @SerializedName("images")
    @Expose
    val images: List<ImageElementModel>? = null,

    @SerializedName("image")
    @Expose
    val image: String? = null,

    @SerializedName("share_link")
    @Expose
    val shareLink: String? = null,


    @SerializedName("time")
    @Expose
    val time: String? = null,


    @SerializedName("user_id")
    @Expose
    val user_id: Int,

    @SerializedName("user_type")
    @Expose
    val user_type: String,


    var isLoading: Boolean = false
): Parcelable

@Parcelize
data class ImageElementModel(
    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("image")
    @Expose
    val image: String? = null,

    @SerializedName("product_id")
    @Expose
    val productId: Int? = null,

    @SerializedName("post_id")
    @Expose
    val postId: Int? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    val uploadedAt: String? = null
): Parcelable