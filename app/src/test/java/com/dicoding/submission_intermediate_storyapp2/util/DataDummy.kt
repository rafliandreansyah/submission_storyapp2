package com.dicoding.submission_intermediate_storyapp2.util

import com.dicoding.submission_intermediate_storyapp2.model.*
import java.util.*


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

fun generateSuccessDummyListStoryLocation(): ResponseListStory {
    val stories = LinkedList<Story>()

    for (i in 1..10) {
        val story = Story(
            id = "id++$i",
            name = "name++$i",
            description = "description++$i",
            photoUrl = "photo++$i.jpg",
            createdAt = "created++$i",
            lat = 6.255176951202816 + i,
            lon = 44.6126015111804 + i
        )
        stories.add(story)
    }

    return ResponseListStory(
        error = false,
        message = "Stories fetched successfully",
        listStory = stories
    )
}

fun generateErrorDummyListStoryLocation(): ResponseListStory {
    return ResponseListStory(
        error = true,
        message = "Missing authentication",
        listStory = null
    )
}

fun generateSuccessDummyDetailStory(): ResponseDetailStory {
    val dataStory = Story(
        id = "id",
        name = "name",
        description = "description",
        photoUrl = "photo.jpg",
        createdAt = "created"
    )
    return ResponseDetailStory(
        error = false,
        message = "Story fetched successfully",
        story = dataStory
    )
}

fun generateErrorDummyDetailStory(): ResponseDetailStory {
    return ResponseDetailStory(
        error = true,
        message = "Story not found"
    )
}


