package com.jacob.wakatimeapp.login.ui

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.jacob.wakatimeapp.core.utils.Utils
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserDetailsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    application: Application,
    private val updateUserDetailsUC: UpdateUserDetailsUC,
    private val ioDispatcher: CoroutineContext,
    private val utils: Utils,
) : AndroidViewModel(application) {
    val authManager: AuthManager = AuthManager(getApplication())

    var authStatus by mutableStateOf(authManager.isLoggedIn())
        private set

    fun updateUserDetails() {
        CoroutineScope(ioDispatcher).launch {
            utils.getFreshToken(getApplication())?.let {
                updateUserDetailsUC(it, getApplication())
            }
        }
    }

    fun exchangeToken(intent: Intent) {
        authManager.exchangeToken(intent)
        authStatus = authManager.isLoggedIn()
    }
}
