package com.jacob.wakatimeapp.login.ui

interface LoginPageNavigator {
    fun toExtractUserDataPage()
    suspend fun toHomePage()
}
