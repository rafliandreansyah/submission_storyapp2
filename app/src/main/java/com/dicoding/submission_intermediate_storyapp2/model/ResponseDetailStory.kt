package com.dicoding.submission_intermediate_storyapp2.model

import com.google.gson.annotations.SerializedName

data class ResponseDetailStory(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("story")
	val story: Story? = null
)
