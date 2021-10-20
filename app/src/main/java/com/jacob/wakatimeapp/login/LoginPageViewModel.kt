package com.jacob.wakatimeapp.login

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.jacob.wakatimeapp.utils.Constants
import com.jacob.wakatimeapp.common.utils.AuthStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import net.openid.appauth.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val authState = AuthState()
    private val authStateManager = AuthStateManager.getInstance(getApplication())
    val authRequest: AuthorizationRequest

    init {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(Constants.authorizationUrl),
            Uri.parse(Constants.tokenUrl)
        )

        val redirectUri = Uri.parse(Constants.redirectUrl)
        authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            Constants.clientId(application),
            ResponseTypeValues.CODE,
            redirectUri
        ).setScopes(Constants.scope)
            .build()
    }

    fun isLoggedIn() = authStateManager.current.isAuthorized

    fun exchangeToken(authService: AuthorizationService, intent: Intent) {
        AuthorizationResponse.fromIntent(intent)?.run {
            authService.performTokenRequest(
                createTokenExchangeRequest(),
                ClientSecretPost(Constants.clientSecret(getApplication()))
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
}
