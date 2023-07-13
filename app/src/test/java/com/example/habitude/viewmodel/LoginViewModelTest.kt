package com.example.habitude.viewmodel

import com.example.habitude.utils.Resource
import com.example.habitude.utils.TaskMock
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

/*
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val mockFirebaseAuth: FirebaseAuth = mock(FirebaseAuth::class.java)

    @Before
    fun setup() {
        viewModel = LoginViewModel(mockFirebaseAuth)
    }

    @Test
    fun `when valid credentials are given, login should be successful`() = runBlockingTest {
        val expectedUser = mock(FirebaseUser::class.java)
        val authResult = mock(AuthResult::class.java)

        whenever(authResult.user).thenReturn(expectedUser)
        whenever(mockFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(TaskMock.makeSuccessfulTask(authResult))

        viewModel.login("example.email@example.com", "Password123")

        val loginResource = viewModel.login.first()

        assertThat(loginResource).isInstanceOf(Resource.Success::class.java)
        assertThat((loginResource as Resource.Success).data).isEqualTo(expectedUser)
    }
}*/
