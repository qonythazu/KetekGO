package com.dicoding.ketekgo.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean? = false,

    @field:SerializedName("uuid")
    val uuid: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("role")
    val role: Int? = null
)