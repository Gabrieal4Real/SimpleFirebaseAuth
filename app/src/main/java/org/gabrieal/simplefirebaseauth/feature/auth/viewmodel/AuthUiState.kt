package org.gabrieal.simplefirebaseauth.feature.auth.viewmodel

data class AuthUiState(
    val email: String = "",
    val errorEmail: String? = null,
    val password: String = "",
    val errorPassword: String? = null,
    val enableButton: Boolean = false,
    val loading: Boolean = false,
    val isRegisterMode: Boolean = false,
    val error: String? = null
)
