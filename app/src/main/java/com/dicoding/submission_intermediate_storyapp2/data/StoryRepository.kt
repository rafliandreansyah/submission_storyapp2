package com.dicoding.submission_intermediate_storyapp2.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.submission_intermediate_storyapp2.api.ApiService
import com.dicoding.submission_intermediate_storyapp2.model.Story
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {

    fun getListStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StorySource(apiService, context)
            }
        ).liveData
    }

}