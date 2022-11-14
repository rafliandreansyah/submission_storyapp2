package com.dicoding.submission_intermediate_storyapp2.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URLConnection

fun createPartFromString(data: String): RequestBody {
    return data.toRequestBody("text/plain".toMediaType())
}

fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
    val name = URLConnection.guessContentTypeFromName(file.name)

    // create RequestBody instance from file
    val requestFile: RequestBody = file.asRequestBody(name.toMediaTypeOrNull())

    // MultipartBody.Part is used to send also the actual file name
    return  MultipartBody.Part.createFormData(partName, file.name, requestFile)
}
