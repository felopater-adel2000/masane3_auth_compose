package com.felo.masane3_test.data.state

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenericResponse<T>(
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("message")
    @Expose
    var message: String?,
    @SerializedName("data")
    @Expose
    var data: T?,

    @SerializedName("errors")
    @Expose
    var errors: List<String>? = null
)