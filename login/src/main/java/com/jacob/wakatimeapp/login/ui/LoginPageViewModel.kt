package com.jacob.wakatimeapp.login.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.core.common.AuthStateManager
import com.jacob.wakatimeapp.core.common.Constants
import com.jacob.wakatimeapp.login.BuildConfig
import com.jacob.wakatimeapp.login.usecases.UpdateUserDetailsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.*
import net.openid.appauth.AuthorizationRequest.Builder
import timber.log.Timber
import timber.log.Timber.Forest
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    application: Application,
    private val updateUserDetailsUC: UpdateUserDetailsUC,
    private val ioDispatcher: CoroutineContext,
    private val authStateManager: AuthStateManager,
) : AndroidViewModel(application) {
    private val authService = AuthorizationService(getApplication())

    private val serviceConfig = AuthorizationServiceConfiguration(
        Uri.parse(Constants.authorizationUrl),
        Uri.parse(Constants.tokenUrl)
    )

    private val authRequest: AuthorizationRequest = Builder(
        serviceConfig,
        BuildConfig.CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(Constants.redirectUrl)
    ).setScopes(Constants.scope)
        .build()


    var authStatus by mutableStateOf(authStateManager.current.isAuthorized)
        private set

    fun updateUserDetails() {
        CoroutineScope(ioDispatcher).launch {
            authStateManager.getFreshToken()
                .filterNotNull()
                .first()
                .let { updateUserDetailsUC.invoke(it) }
        }
    }

    fun getAuthIntent(): Intent? = authService.getAuthorizationRequestIntent(authRequest)

    fun exchangeToken(intent: Intent) {
        val authorizationResponse = AuthorizationResponse.fromIntent(intent) ?: run {
            Timber.e("Empty auth intent")
            return
        }

        authService.performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            ClientSecretPost(BuildConfig.CLIENT_SECRET)
        ) { tokenResponse, authorizationException ->
            authorizationException?.let(Forest::e)
            viewModelScope.launch {
                authStateManager.updateAfterTokenResponse(tokenResponse, authorizationException)
                authStatus = authStateManager.current.isAuthorized
            }
        }
    }
}
