package com.anthill.app.ui.auth

import com.anthill.app.data.repository.AuthRepository
import com.anthill.app.data.dao.UserDao
import com.anthill.app.data.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class AuthViewModelTest {

    private val userDao = mock(UserDao::class.java)
    private val repo = AuthRepository(userDao)
    private val viewModel = AuthViewModel(repo)

    @Test
    fun `should set loggedIn state when login credentials are valid`() = runBlocking {
        val user = User(id=1, username="sadra", passwordHash="pass", fullName="Sadra", createdBy="admin", updatedBy="admin", createdAt=0, updatedAt=0)
        `when`(userDao.getUserByUsername("sadra")).thenReturn(user)
        
        viewModel.login("sadra", "pass")
        
        val state = viewModel.uiState.value
        assert(state is AuthState.LoggedIn)
        assertEquals("sadra", (state as AuthState.LoggedIn).username)
    }

    @Test
    fun `should set error state when user does not exist`() = runBlocking {
        `when`(userDao.getUserByUsername("sadra")).thenReturn(null)
        
        viewModel.login("sadra", "any_pass")
        
        val state = viewModel.uiState.value
        assert(state is AuthState.Error)
        assertEquals("Invalid credentials", (state as AuthState.Error).message)
    }
    
    @Test
    fun `should set error state when password does not match`() = runBlocking {
        val user = User(id=1, username="sadra", passwordHash="pass", fullName="Sadra", createdBy="admin", updatedBy="admin", createdAt=0, updatedAt=0)
        `when`(userDao.getUserByUsername("sadra")).thenReturn(user)
        
        viewModel.login("sadra", "wrong_pass")
        
        val state = viewModel.uiState.value
        assert(state is AuthState.Error)
        assertEquals("Invalid credentials", (state as AuthState.Error).message)
    }

    @Test
    fun `should set error state when username is blank`() = runBlocking {
        viewModel.login("", "pass")
        
        val state = viewModel.uiState.value
        assert(state is AuthState.Error)
    }

    @Test
    fun `should set error state when password is blank`() = runBlocking {
        viewModel.login("sadra", "")
        
        val state = viewModel.uiState.value
        assert(state is AuthState.Error)
    }
}
