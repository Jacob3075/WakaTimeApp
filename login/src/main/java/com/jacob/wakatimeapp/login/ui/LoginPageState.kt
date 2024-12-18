package com.jacob.wakatimeapp.login.ui

sealed class LoginPageState {
    data object Idle : LoginPageState()
    data object Loading : LoginPageState()
    data object NewLoginSuccess : LoginPageState()
    data object ExistingLoginSuccess : LoginPageState()
    data class Error(val message: String) : LoginPageState()
}
