package com.jacob.wakatimeapp.login.ui

sealed class LoginPageState {
    data object Idle : LoginPageState()
    data object Loading : LoginPageState()
    data object Success : LoginPageState()
    data class Error(val message: String) : LoginPageState()
}
