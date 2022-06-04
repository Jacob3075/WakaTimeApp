package com.jacob.wakatimeapp.login.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jacob.wakatimeapp.core.utils.AuthStateManager
import com.jacob.wakatimeapp.core.utils.Constants
import com.jacob.wakatimeapp.core.utils.clientId
import com.jacob.wakatimeapp.core.utils.clientSecret
import net.openid.appauth.*
import net.openid.appauth.AuthorizationRequest.Builder
import timber.log.Timber

class AuthManager(private val context: Context) {
    private val authState = AuthStateManager.getInstance(context)
    private val authService = AuthorizationService(context)

    private val serviceConfig = AuthorizationServiceConfiguration(
        Uri.parse(Constants.authorizationUrl),
        Uri.parse(Constants.tokenUrl)

    )
    private val authRequest: AuthorizationRequest = Builder(
        serviceConfig,
        context.clientId(),
        ResponseTypeValues.CODE,
        Uri.parse(Constants.redirectUrl)
    ).setScopes(Constants.scope)
        .build()

    fun getAuthIntent(): Intent? = authService.getAuthorizationRequestIntent(authRequest)

    fun exchangeToken(intent: Intent) {
         AuthorizationResponse.fromIntent(intent)?.run {
            return@run authService.performTokenRequest(
                createTokenExchangeRequest(),
                ClientSecretPost(context.clientSecret())
            ) { tokenResponse, authorizationException ->
                tokenResponse?.let {
                    authState.updateAfterTokenResponse(it, authorizationException)
                } ?: run {
                    Timber.e(authorizationException)
                    authState.updateAfterTokenResponse(null, authorizationException)
                }
            }
        }
    }

    fun isLoggedIn() = authState.current.isAuthorized
}
