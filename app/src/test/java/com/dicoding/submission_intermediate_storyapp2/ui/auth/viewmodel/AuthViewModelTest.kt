package com.dicoding.submission_intermediate_storyapp2.ui.auth.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submission_intermediate_storyapp2.data.AuthRepository
import com.dicoding.submission_intermediate_storyapp2.model.LoginResponse
import com.dicoding.submission_intermediate_storyapp2.model.ResponseGeneral
import com.dicoding.submission_intermediate_storyapp2.util.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var authViewModel: AuthViewModel


    companion object{
        private const val LOGIN_CORRECT_EMAIL = "rafli@gmail.com"
        private const val LOGIN_CORRECT_PASSWORD = "secured"
        private const val LOGIN_WRONG_PASSWORD = "not secured"

        private const val REGISTER_NAME = "Rafli"
        private const val REGISTER_EMAIL = "rafli@gmail.com"
        private const val REGISTER_EXISTING_EMAIL = "rafli123@gmail.com"
        private const val REGISTER_PASSWORD = "password"
    }

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(authRepository)
    }

    @Test
    fun `When post login it returns Result Success and success response data`() {
        val dummySuccessResponse = generateSuccessLoginResponse()

        val expectedValues = MutableLiveData<Result<LoginResponse>>()
        expectedValues.value = Result.Success(data = dummySuccessResponse)

        `when`(authRepository.login(LOGIN_CORRECT_EMAIL, LOGIN_CORRECT_PASSWORD)).thenReturn(expectedValues)

        val actualResponse = authViewModel.login(LOGIN_CORRECT_EMAIL, LOGIN_CORRECT_PASSWORD).getOrAwaitValue()

        Mockito.verify(authRepository).login(LOGIN_CORRECT_EMAIL, LOGIN_CORRECT_PASSWORD)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(expectedValues.value, actualResponse)
    }

    @Test
    fun `When post login it return Result Error and wrong password message`() {
        val dummyWrongPasswordResponse = generateErrorLoginResponse()

        val expectedValues = MutableLiveData<Result<LoginResponse>>()
        expectedValues.value = Result.Error(code = 401, data = dummyWrongPasswordResponse)

        `when`(authRepository.login(LOGIN_CORRECT_EMAIL, LOGIN_WRONG_PASSWORD)).thenReturn(expectedValues)

        val actualResponse = authViewModel.login(LOGIN_CORRECT_EMAIL, LOGIN_WRONG_PASSWORD).getOrAwaitValue()
        Mockito.verify(authRepository).login(LOGIN_CORRECT_EMAIL, LOGIN_WRONG_PASSWORD)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
        assertEquals((expectedValues.value as Result.Error).data?.message, actualResponse.data?.message)

    }

    @Test
    fun `When register then return Result success and success response data`() {
        val dummySuccessRegisterResponse = generateSuccessRegisterResponse()

        val expectedValues = MutableLiveData<Result<ResponseGeneral>>()
        expectedValues.value = Result.Success(dummySuccessRegisterResponse)

        `when`(authRepository.register(REGISTER_NAME, REGISTER_EMAIL, REGISTER_PASSWORD)).thenReturn(expectedValues)

        val actualResponse = authViewModel.register(REGISTER_NAME, REGISTER_EMAIL, REGISTER_PASSWORD).getOrAwaitValue()
        Mockito.verify(authRepository).register(REGISTER_NAME, REGISTER_EMAIL, REGISTER_PASSWORD)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(expectedValues.value, actualResponse)

    }

    @Test
    fun `When register then return Result Error and email already exist`() {
        val dummyErrorRegisterResponse = generateErrorRegisterResponse()

        val expectedValues = MutableLiveData<Result<ResponseGeneral>>()
        expectedValues.value = Result.Error(code = 400, data = dummyErrorRegisterResponse)

        `when`(authRepository.register(REGISTER_NAME, REGISTER_EXISTING_EMAIL, REGISTER_PASSWORD)).thenReturn(expectedValues)

        val actualResponse = authViewModel.register(REGISTER_NAME, REGISTER_EXISTING_EMAIL, REGISTER_PASSWORD).getOrAwaitValue()
        Mockito.verify(authRepository).register(REGISTER_NAME, REGISTER_EXISTING_EMAIL, REGISTER_PASSWORD)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
        assertEquals((expectedValues.value as Result.Error).data?.message, actualResponse.data?.message)

    }

}