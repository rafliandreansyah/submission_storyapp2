package com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.submission_intermediate_storyapp2.api.ApiConfig
import com.dicoding.submission_intermediate_storyapp2.constant.PREF_TOKEN
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

@HiltViewModel
@Suppress("DEPRECATION")
class StoryViewModel @Inject constructor(private val repository: StoryRepository) : ViewModel() {

    val listStoryData: MutableLiveData<ResponseListStory> by lazy {
        MutableLiveData<ResponseListStory>()
    }

    val addStoryData: MutableLiveData<ResponseGeneral> by lazy {
        MutableLiveData<ResponseGeneral>()
    }

    val detailStoryData: MutableLiveData<ResponseDetailStory> by lazy {
        MutableLiveData<ResponseDetailStory>()
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

    /*fun getAllStory() {
        try {
            listStoryData.value = null
            val bearer = "Bearer ${token as String}"
            ApiConfig.getApiService().getListStory(bearer).enqueue(object : Callback<ResponseListStory>{
                override fun onResponse(
                    call: Call<ResponseListStory>,
                    response: Response<ResponseListStory>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.error == false) {
                            listStoryData.postValue(response.body())
                        }
                        else {
                            if (response.body()?.message != null && response.body()!!.message.isNotEmpty()) {
                                listStoryData.postValue(ResponseListStory(null, true, response.message()))
                            } else {
                                listStoryData.postValue(ResponseListStory(null, true, "error get data"))
                            }
                        }
                    } else if (response.code() == 401) {
                        listStoryData.postValue(ResponseListStory(null, true, "unauthorized"))
                    } else {
                        listStoryData.postValue(ResponseListStory(null, true, "error get data"))
                    }
                }

                override fun onFailure(call: Call<ResponseListStory>, t: Throwable) {
                    t.printStackTrace()
                    listStoryData.postValue(ResponseListStory(null, true, "error get data"))
                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    fun getListStory(): LiveData<PagingData<Story>> = repository.getListStory().cachedIn(viewModelScope)

    fun getDetailStory(id: String) {
        try {
            detailStoryData.value = null
            val bearer = "Bearer ${token as String}"

            ApiConfig.getApiService().getDetailStory(bearer, id).enqueue(object: Callback<ResponseDetailStory>{
                override fun onResponse(
                    call: Call<ResponseDetailStory>,
                    response: Response<ResponseDetailStory>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.error == false) {
                            detailStoryData.postValue(response.body())
                        }
                        else {
                            if (response.body()?.message != null && response.body()!!.message.isNotEmpty()) {
                                detailStoryData.postValue(ResponseDetailStory(true, response.message(), null))
                            } else {
                                detailStoryData.postValue(ResponseDetailStory(true, "error get data", null))
                            }
                        }
                    } else if (response.code() == 401) {
                        detailStoryData.postValue(ResponseDetailStory(true, "unauthorized", null))
                    } else {
                        detailStoryData.postValue(ResponseDetailStory(true, "error get data", null))
                    }
                }

                override fun onFailure(call: Call<ResponseDetailStory>, t: Throwable) {
                    detailStoryData.postValue(ResponseDetailStory(true, "error get data", null))
                }

            })

        }catch (e: Exception) {
            e.printStackTrace()
            detailStoryData.postValue(ResponseDetailStory(true, "error get data", null))
        }
    }

}