package org.gabrieal.simplefirebaseauth.feature.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.simplefirebaseauth.helper.isEmailValid
import org.gabrieal.simplefirebaseauth.helper.isPasswordValid


class AuthViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun verifyAndSaveEmail(email: String) {
        _uiState.update { it.copy(email = email) }
        verifyButtonEnabled()

        if (!email.isEmailValid() && email.isNotEmpty()) {
            _uiState.update { it.copy(errorEmail = "Invalid email") }
            return
        }

        _uiState.update { it.copy(errorEmail = null) }
    }

    fun verifyAndSavePassword(password: String) {
        _uiState.update { it.copy(password = password) }
        verifyButtonEnabled()

        if (!password.isPasswordValid() && password.isNotEmpty()) {
            _uiState.update { it.copy(errorPassword = "Password must be at least 6 characters") }
            return
        }

        _uiState.update { it.copy(errorPassword = null) }
    }

    fun verifyButtonEnabled() {
        _uiState.update { it.copy(enableButton = uiState.value.email.isEmailValid() && uiState.value.password.isPasswordValid()) }
    }

    fun setLoading(loading: Boolean) {
        _uiState.update { it.copy(loading = loading) }
    }

    fun toggleRegister() {
        _uiState.update { it.copy(isRegisterMode = !uiState.value.isRegisterMode) }
    }

    fun authenticateUser() {
        setLoading(true)
        auth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnSuccessListener {
                Log.d("AuthViewModel", "Authentication successful")
                setLoading(false)
            }
            .addOnFailureListener {
                Log.d("AuthViewModel", "Authentication failed ${it.message}")
                setLoading(false)
                setError(it.message)
            }
    }

    fun registerViaFirebase() {
        setLoading(true)
        auth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnSuccessListener { task ->
                Log.d("AuthViewModel", "Authentication successful")
                setLoading(false)
            }
            .addOnFailureListener {
                Log.d("AuthViewModel", "Authentication failed ${it.message}")
                setLoading(false)
                setError(it.message)
            }
    }

    fun setError(message: String?) {
        _uiState.update { it.copy(error = message) }
    }
}
