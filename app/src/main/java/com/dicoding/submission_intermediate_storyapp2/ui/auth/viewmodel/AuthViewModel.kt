package com.dicoding.submission_intermediate_storyapp2.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submission_intermediate_storyapp2.data.AuthRepository
import com.dicoding.submission_intermediate_storyapp2.model.LoginResponse
import com.dicoding.submission_intermediate_storyapp2.model.ResponseGeneral
import com.dicoding.submission_intermediate_storyapp2.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> =
        authRepository.login(email, password)

    fun register(name: String, email: String, password: String): LiveData<Result<ResponseGeneral>> =
        authRepository.register(name, email, password)

}