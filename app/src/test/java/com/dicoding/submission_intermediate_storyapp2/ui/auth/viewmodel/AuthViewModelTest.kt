package com.dicoding.submission_intermediate_storyapp2.ui.auth.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submission_intermediate_storyapp2.data.AuthRepository
import com.dicoding.submission_intermediate_storyapp2.model.LoginResponse
import com.dicoding.submission_intermediate_storyapp2.util.generateErrorLoginResponse
import com.dicoding.submission_intermediate_storyapp2.util.generateSuccessLoginResponse
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.dicoding.submission_intermediate_storyapp2.util.Result
import com.dicoding.submission_intermediate_storyapp2.util.getOrAwaitValue
import org.junit.Rule
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var authViewModel: AuthViewModel
    private val dummySuccessResponse = generateSuccessLoginResponse()
    private val dummyWrongPasswordResponse = generateErrorLoginResponse()


    companion object{
        private const val CORRECT_EMAIL = "rafli@gmail.com"
        private const val CORRECT_PASSWORD = "secured"

        private const val WRONG_PASSWORD = "not secured"
    }

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(authRepository)
    }

    @Test
    fun `When post login it returns Result Success and success response data`() {
        val actualValues = MutableLiveData<Result<LoginResponse>>()
        actualValues.value = Result.Success(data = dummySuccessResponse)

        `when`(authRepository.login(CORRECT_EMAIL, CORRECT_PASSWORD)).thenReturn(actualValues)

        val actualResponse = authViewModel.login(CORRECT_EMAIL, CORRECT_PASSWORD).getOrAwaitValue()

        Mockito.verify(authRepository).login(CORRECT_EMAIL, CORRECT_PASSWORD)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(actualResponse, actualValues.value)
    }

    @Test
    fun `When post login it return Result Error and wrong password message`() {
        val actualValues = MutableLiveData<Result<LoginResponse>>()
        actualValues.value = Result.Error(message = generateErrorLoginResponse().message, code = 401)

        `when`(authRepository.login(CORRECT_EMAIL, WRONG_PASSWORD)).thenReturn(actualValues)

        val actualResponse = authViewModel.login(CORRECT_EMAIL, WRONG_PASSWORD).getOrAwaitValue()
        Mockito.verify(authRepository).login(CORRECT_EMAIL, WRONG_PASSWORD)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
        assertEquals(actualResponse.message,  (actualValues.value as Result.Error).message)

    }

}