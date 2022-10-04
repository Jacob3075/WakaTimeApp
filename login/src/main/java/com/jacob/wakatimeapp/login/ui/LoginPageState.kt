package com.jacob.wakatimeapp.login.ui

sealed class LoginPageState {
    object Idle : LoginPageState()
    object Loading : LoginPageState()
    object Success : LoginPageState()
    data class Error(val message: String) : LoginPageState()
}
