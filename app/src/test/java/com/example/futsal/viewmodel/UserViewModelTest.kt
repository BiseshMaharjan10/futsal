package com.example.futsal.viewmodel

import com.example.futsal.repository.UserRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelTest {

    @Test
    fun login_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        var successResult = false
        var messageResult = ""

        viewModel.login("test@gmail.com", "123456") { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(successResult)
        assertEquals("Login success", messageResult)

        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }

    @Test
    fun register_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, String) -> Unit>(2)
            callback(true, "Registration success", "user123")
            null
        }.`when`(repo).register(eq("new@gmail.com"), eq("password123"), any())

        var successResult = false
        var messageResult = ""
        var userIdResult = ""

        viewModel.register("new@gmail.com", "password123") { success, msg, userId ->
            successResult = success
            messageResult = msg
            userIdResult = userId
        }

        assertTrue(successResult)
        assertEquals("Registration success", messageResult)
        assertEquals("user123", userIdResult)

        verify(repo).register(eq("new@gmail.com"), eq("password123"), any())
    }
}
