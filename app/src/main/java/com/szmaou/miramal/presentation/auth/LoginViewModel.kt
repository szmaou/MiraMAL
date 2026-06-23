package com.szmaou.miramal.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szmaou.miramal.data.remote.auth.AuthEventBus
import com.szmaou.miramal.data.remote.auth.MalAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: MalAuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState(isLoggedIn = authManager.isLoggedIn))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        observeAuthRedirect()
    }

    private fun observeAuthRedirect() {
        viewModelScope.launch {
            AuthEventBus.events.collect { code ->
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                try {
                    authManager.exchangeCode(code)
                    _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true)
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Login failed"
                    )
                }
            }
        }
    }

    fun getAuthUrl(): String = authManager.buildAuthUrl()

    fun isLoggedIn(): Boolean = authManager.isLoggedIn

    fun logout() {
        authManager.logout()
        _uiState.value = LoginUiState(isLoggedIn = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
