package com.dicoding.submission_intermediate_storyapp2.util

import com.dicoding.submission_intermediate_storyapp2.model.LoginResponse
import com.dicoding.submission_intermediate_storyapp2.model.LoginResult
import com.dicoding.submission_intermediate_storyapp2.model.ResponseGeneral


fun generateSuccessLoginResponse(): LoginResponse {
    val loginResult = LoginResult(
        userId = "user1122",
        name = "rafli andreansyah",
        token = "test token"
    )
    return LoginResponse(
        error = false,
        message = "success",
        loginResult = loginResult
    )
}

fun generateErrorLoginResponse(): LoginResponse {
    return LoginResponse(
        error = true,
        message = "Invalid password"
    )
}

fun generateSuccessRegisterResponse(): ResponseGeneral {
    return ResponseGeneral(
        error = false,
        message = "User created"
    )
}

fun generateErrorRegisterResponse(): ResponseGeneral {
    return ResponseGeneral(
        error = true,
        message = "Email is already taken"
    )
}