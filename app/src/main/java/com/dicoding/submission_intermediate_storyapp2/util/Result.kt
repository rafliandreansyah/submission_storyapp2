package com.dicoding.submission_intermediate_storyapp2.util

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int? = null
) {
    class Loading<T>: Result<T>()
    class Success<T>(data: T): Result<T>(data = data)
    class Error<T>(message: String? = "error get data", code: Int?, data: T?): Result<T>(message = message, code = code, data = data)
}