package com.felo.masane3_test.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    @SerializedName("user")
    @Expose
    val user: User? = null,

    @SerializedName("token")
    @Expose
    val token: String? = null
) : Parcelable

@Parcelize
data class User(
    @SerializedName("id")
    @Expose
    val id: Int? = 0,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("phone")
    @Expose
    val phone: String? = null,

    @SerializedName("device_token")
    @Expose
    val deviceToken: String? = null,

    @SerializedName("image")
    @Expose
    val image: String? = null,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("age")
    @Expose
    val age: String? = null,

    @SerializedName("job")
    @Expose
    val job: String? = null,

    @SerializedName("company_id")
    @Expose
    val companyId: Int? = 0,

    var isSelected: Boolean = false
): Parcelable