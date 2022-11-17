package com.dicoding.submission_intermediate_storyapp2.data

import android.content.Context
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.submission_intermediate_storyapp2.api.ApiService
import com.dicoding.submission_intermediate_storyapp2.constant.PREF_TOKEN
import com.dicoding.submission_intermediate_storyapp2.model.ResponseDetailStory
import com.dicoding.submission_intermediate_storyapp2.model.ResponseGeneral
import com.dicoding.submission_intermediate_storyapp2.model.ResponseListStory
import com.dicoding.submission_intermediate_storyapp2.model.Story
import com.dicoding.submission_intermediate_storyapp2.util.Result
import com.dicoding.submission_intermediate_storyapp2.util.convertErrorData
import com.dicoding.submission_intermediate_storyapp2.util.createPartFromString
import com.dicoding.submission_intermediate_storyapp2.util.prepareFilePart
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {
    private val authorization = "Bearer ${PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_TOKEN, "")}";

    fun getListStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StorySource(apiService, authorization)
            }
        ).liveData
    }

    fun getListStoryLocation(): LiveData<Result<ResponseListStory>> {
        val data: MutableLiveData<Result<ResponseListStory>> = MutableLiveData()
        data.postValue(Result.Loading())

        try {
            apiService.getListStory(authorization, 1).enqueue(object : Callback<ResponseListStory>{
                override fun onResponse(
                    call: Call<ResponseListStory>,
                    response: Response<ResponseListStory>
                ) {
                    if (response.isSuccessful) {
                        data.postValue(Result.Success(response.body() as ResponseListStory))
                    }
                    else {
                        val errorData = response.errorBody()?.string()?.let { convertErrorData(it) }
                        data.postValue(Result.Error(errorData?.message ?: "error get data", response.code()))
                    }
                }

                override fun onFailure(call: Call<ResponseListStory>, t: Throwable) {
                    data.postValue(Result.Error(t.message.toString(), null))
                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
            data.postValue(Result.Error("error convert data", null))
        }


        return data
    }

    fun getDetailStory(id: String): LiveData<Result<ResponseDetailStory>> {
        val data: MutableLiveData<Result<ResponseDetailStory>> = MutableLiveData()

        try {
            apiService.getDetailStory(authorization, id).enqueue(object: Callback<ResponseDetailStory>{
                override fun onResponse(
                    call: Call<ResponseDetailStory>,
                    response: Response<ResponseDetailStory>
                ) {
                    if (response.isSuccessful) {
                        data.postValue(Result.Success(response.body() as ResponseDetailStory))
                    }
                    else {
                        val errorData = response.errorBody()?.string()?.let { convertErrorData(it) }
                        data.postValue(Result.Error(errorData?.message ?: "error get data", response.code()))
                    }
                }

                override fun onFailure(call: Call<ResponseDetailStory>, t: Throwable) {
                    data.postValue(Result.Error(t.message.toString(), null))
                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
            data.postValue(Result.Error("error convert data", null))
        }


        return data
    }

    fun createStory(desc: String, file: File): LiveData<Result<ResponseGeneral>> {
        val data: MutableLiveData<Result<ResponseGeneral>> = MutableLiveData()
        try {
            val description = createPartFromString(desc)
            val storyFile = prepareFilePart("photo", file)
            apiService.addStory(authorization, description, storyFile).enqueue(object: Callback<ResponseGeneral> {
                override fun onResponse(
                    call: Call<ResponseGeneral>,
                    response: Response<ResponseGeneral>
                ) {
                    if (response.isSuccessful) {
                        data.postValue(Result.Success(response.body() as ResponseGeneral))
                    }
                    else {
                        val errorData = response.errorBody()?.string()?.let { convertErrorData(it) }
                        data.postValue(Result.Error(errorData?.message ?: "error get data", response.code()))
                    }
                }

                override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                    data.postValue(Result.Error(t.message.toString(), null))
                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
            data.postValue(Result.Error("error convert data", null))
        }

        return data
    }

}