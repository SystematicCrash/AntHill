package com.anthill.app.ui.auth

import com.anthill.app.data.repository.AuthRepository
import com.anthill.app.data.dao.UserDao
import com.anthill.app.data.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class AuthViewModelTest {

    @Test
    fun loginSuccess() = runBlocking {
        val userDao = mock(UserDao::class.java)
        val user = User(id=1, username="sadra", passwordHash="pass", fullName="Sadra", createdBy="admin", updatedBy="admin", createdAt=0, updatedAt=0)
        `when`(userDao.getUserByUsername("sadra")).thenReturn(user)
        
        val repo = AuthRepository(userDao)
        val viewModel = AuthViewModel(repo)
        
        viewModel.login("sadra", "pass")
        
        val state = viewModel.uiState.value
        assert(state is AuthState.LoggedIn)
        assertEquals("sadra", (state as AuthState.LoggedIn).username)
    }
}
