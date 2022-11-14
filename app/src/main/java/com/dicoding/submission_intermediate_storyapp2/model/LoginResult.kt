package com.dicoding.submission_intermediate_storyapp2.model


import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("name")
    var name: String,
    @SerializedName("token")
    var token: String,
    @SerializedName("userId")
    var userId: String
)