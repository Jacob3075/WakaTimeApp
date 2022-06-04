package com.jacob.wakatimeapp.login.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
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

    fun getAuthIntent(): Intent? = authService.getAuthorizationRequestIntent(authRequest)

    fun exchangeToken(intent: Intent) {
        AuthorizationResponse.fromIntent(intent)?.run {
            authService.performTokenRequest(
                createTokenExchangeRequest(),
                ClientSecretPost(getApplication<WakaTimeApp>().clientSecret())
            ) { tokenResponse, authorizationException ->
                tokenResponse?.let {
                    authStateManager.updateAfterTokenResponse(it, authorizationException)
                } ?: run {
                    Timber.e(authorizationException)
                    authStateManager.updateAfterTokenResponse(null, authorizationException)
                }
            }
        }
    }

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
