package com.dicoding.submission_intermediate_storyapp2.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("email")
    var email: String? = null,

    @field:SerializedName("password")
    var password: String? = null,
)