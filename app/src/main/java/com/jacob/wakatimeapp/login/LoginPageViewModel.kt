package com.jacob.wakatimeapp.login

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.jacob.data.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import net.openid.appauth.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor() : ViewModel() {
    private val authState = AuthState()
    val authRequest: AuthorizationRequest

    init {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(Constants.authorizationUrl),
            Uri.parse(Constants.tokenUrl)
        )

        val redirectUri = Uri.parse(Constants.redirectUrl)
        authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            Constants.clientId,
            ResponseTypeValues.CODE,
            redirectUri
        ).setScopes(Constants.scope)
            .build()
    }

    fun exchangeToken(authService: AuthorizationService, intent: Intent) {
        AuthorizationResponse.fromIntent(intent)?.run {
            authService.performTokenRequest(
                createTokenExchangeRequest(),
                ClientSecretPost(Constants.clientSecret)
            ) { tokenResponse, authorizationException ->
                tokenResponse?.let {
                    authState.update(it, authorizationException)
                } ?: run {
                    Timber.e(authorizationException)
                }
            }
        }
    }
}
