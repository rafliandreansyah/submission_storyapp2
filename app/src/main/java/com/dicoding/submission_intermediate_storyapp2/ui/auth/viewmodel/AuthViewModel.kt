package com.dicoding.submission_intermediate_storyapp2.ui.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submission_intermediate_storyapp2.api.ApiConfig
import com.dicoding.submission_intermediate_storyapp2.model.LoginResponse
import com.dicoding.submission_intermediate_storyapp2.model.ResponseGeneral
import com.dicoding.submission_intermediate_storyapp2.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AuthViewModel: ViewModel() {

    val loginData: MutableLiveData<LoginResponse> by lazy {
        MutableLiveData<LoginResponse>()
    }

    val registerData: MutableLiveData<ResponseGeneral> by lazy {
        MutableLiveData<ResponseGeneral>()
    }

    fun login(email: String, password: String) {
        try {
            loginData.value = null
            val userLoginData = UserModel(email = email, password = password)
            val service = ApiConfig.getApiService().login(userLoginData)
            service.enqueue(object : Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        loginData.postValue(response.body())
                    }
                    else if (response.code() == 401) {
                        loginData.postValue(LoginResponse(error = true, message = "Wrong email or password"))
                    }
                    else {
                        loginData.postValue(LoginResponse(error = true, message = "error get data"))
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    t.printStackTrace()
                    loginData.postValue(LoginResponse(error = true, message = "error get data"))
                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
            loginData.postValue(LoginResponse(error = true, message = "error get data"))

        }

    }

    fun register(name: String, email: String, password:String) {
        try {
            registerData.value = null
            val userRegisterData = UserModel(name, email, password)
            val service = ApiConfig.getApiService().register(userRegisterData)
            service.enqueue(object : Callback<ResponseGeneral>{
                override fun onResponse(
                    call: Call<ResponseGeneral>,
                    response: Response<ResponseGeneral>
                ) {
                    if (response.isSuccessful) {
                        registerData.postValue(response.body())
                    }
                    else if (response.code() == 400) {
                        registerData.postValue(ResponseGeneral(error = true, "Email is already taken"))
                    }
                    else {
                        registerData.postValue(ResponseGeneral(error = true, "error get data"))

                    }
                }

                override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                    t.printStackTrace()
                    registerData.postValue(ResponseGeneral(error = true, "error get data"))

                }

            })
        }catch (e: Exception) {
            e.printStackTrace()
            registerData.postValue(ResponseGeneral(error = true, "error get data"))

        }
    }

}