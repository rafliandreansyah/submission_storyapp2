package com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.submission_intermediate_storyapp2.api.ApiConfig
import com.dicoding.submission_intermediate_storyapp2.data.StoryRepository
import com.dicoding.submission_intermediate_storyapp2.model.ResponseDetailStory
import com.dicoding.submission_intermediate_storyapp2.model.ResponseGeneral
import com.dicoding.submission_intermediate_storyapp2.model.ResponseListStory
import com.dicoding.submission_intermediate_storyapp2.model.Story
import com.dicoding.submission_intermediate_storyapp2.util.createPartFromString
import com.dicoding.submission_intermediate_storyapp2.util.prepareFilePart
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import com.dicoding.submission_intermediate_storyapp2.util.Result

@HiltViewModel
@Suppress("DEPRECATION")
class StoryViewModel @Inject constructor(private val repository: StoryRepository) : ViewModel() {

    val addStoryData: MutableLiveData<ResponseGeneral> by lazy {
        MutableLiveData<ResponseGeneral>()
    }

    val token = ""

    fun addStory(desc: String, file: File) {
        try {
            addStoryData.value = null
            val bearer = "Bearer ${token as String}"
            val description = createPartFromString(desc)
            val storyFile = prepareFilePart("photo", file)
            ApiConfig.getApiService().addStory(bearer, description, storyFile).enqueue(object : Callback<ResponseGeneral>{
                override fun onResponse(
                    call: Call<ResponseGeneral>,
                    response: Response<ResponseGeneral>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.error == false) {
                            addStoryData.postValue(response.body())
                        }
                        else {
                            if (response.body()?.message != null && response.body()!!.message.isNotEmpty()) {
                                addStoryData.postValue(ResponseGeneral(true, response.message()))
                            } else {
                                addStoryData.postValue(ResponseGeneral(true, "error get data"))
                            }
                        }
                    } else if (response.code() == 401) {
                        addStoryData.postValue(ResponseGeneral(true, "unauthorized"))
                    } else {
                        addStoryData.postValue(ResponseGeneral(true, "error get data"))
                    }
                }

                override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                    t.printStackTrace()
                    addStoryData.postValue(ResponseGeneral(true, "error get data"))
                }

            })
        }catch (e: Exception) {
            addStoryData.postValue(ResponseGeneral(true, "error convert data"))
            e.printStackTrace()
        }
    }

    fun getListStory(): LiveData<PagingData<Story>> = repository.getListStory().cachedIn(viewModelScope)

    fun getListStoryLocation(): LiveData<Result<ResponseListStory>> = repository.getListStoryLocation()

    fun getDetailStory(id: String): LiveData<Result<ResponseDetailStory>> = repository.getDetailStory(id)


}