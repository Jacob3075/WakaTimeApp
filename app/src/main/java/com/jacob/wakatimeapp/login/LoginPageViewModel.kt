package com.jacob.wakatimeapp.login

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.jacob.data.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import net.openid.appauth.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
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
            Constants.clientId(application),
            ResponseTypeValues.CODE,
            redirectUri
        ).setScopes(Constants.scope)
            .build()
    }

    fun exchangeToken(authService: AuthorizationService, intent: Intent) {
        AuthorizationResponse.fromIntent(intent)?.run {
            authService.performTokenRequest(
                createTokenExchangeRequest(),
                ClientSecretPost(Constants.clientSecret(getApplication()))
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
