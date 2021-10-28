package com.jacob.wakatimeapp.login.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.jacob.wakatimeapp.common.utils.AuthStateManager
import com.jacob.wakatimeapp.common.utils.Constants
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserDetailsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import net.openid.appauth.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    application: Application,
    private val updateUserDetailsUC: UpdateUserDetailsUC,
    private val ioDispatcher: CoroutineContext,
) : AndroidViewModel(application) {
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

    @ExperimentalCoroutinesApi
    fun updateUserDetails(authorizationService: AuthorizationService) {
        authStateManager.current.performActionWithFreshTokens(authorizationService) { accessToken, _, authorizationException ->
            if (authorizationException == null) {
                CoroutineScope(ioDispatcher).launch {
                    accessToken?.let {
                        updateUserDetailsUC(it, getApplication())
                    }
                }
            }
        }
    }
}
