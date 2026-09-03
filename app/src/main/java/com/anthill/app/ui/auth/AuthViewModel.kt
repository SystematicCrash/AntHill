package com.anthill.app.ui.auth

import androidx.lifecycle.ViewModel
import com.anthill.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val uiState: StateFlow<AuthState> = _uiState

    suspend fun login(username: String, password: String) {
        val user = repository.login(username, password)
        if (user != null) {
            _uiState.value = AuthState.LoggedIn(user.username)
        } else {
            _uiState.value = AuthState.Error("Invalid credentials")
        }
    }
}

sealed class AuthState {
    object LoggedOut : AuthState()
    data class LoggedIn(val username: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
