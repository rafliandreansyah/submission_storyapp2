package com.dicoding.submission_intermediate_storyapp2.util

import com.dicoding.submission_intermediate_storyapp2.model.LoginResponse
import com.dicoding.submission_intermediate_storyapp2.model.LoginResult
import com.dicoding.submission_intermediate_storyapp2.model.ResponseGeneral
import com.dicoding.submission_intermediate_storyapp2.model.Story


val ERROR_MESSAGE = "error get data"

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

fun generateSuccessDummyListStoryResponse(): List<Story> {
    val listStory: MutableList<Story> = arrayListOf()
    for (i in 1..5) {
        val story = Story(
            id = "id++$i",
            name = "name++$i",
            description = "description++$i",
            photoUrl = "photo++$i.jpg",
            createdAt = "created++$i"
        )

        listStory.add(story)
    }

    return listStory
}

fun generateSuccessDummyCreateStory(): ResponseGeneral {
    return ResponseGeneral(
        error = false,
        message = "Story created successfully"
    )
}

fun generateErrorDummyCreateStory(): ResponseGeneral {
    return ResponseGeneral(
        error = false,
        message = "photo should be Readable"
    )
}


