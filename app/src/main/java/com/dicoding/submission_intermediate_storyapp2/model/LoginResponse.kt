package com.dicoding.submission_intermediate_storyapp2.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    var error: Boolean,
    @SerializedName("loginResult")
    var loginResult: LoginResult? = null,
    @SerializedName("message")
    var message: String
)