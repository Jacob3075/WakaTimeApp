package com.jacob.wakatimeapp.login.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.WakaTimeApp
import com.jacob.wakatimeapp.core.utils.AuthStateManager
import com.jacob.wakatimeapp.core.utils.Constants
import com.jacob.wakatimeapp.core.utils.clientId
import com.jacob.wakatimeapp.core.utils.clientSecret
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserDetailsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.*
import net.openid.appauth.AuthorizationRequest.Builder
import timber.log.Timber
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
        getApplication<WakaTimeApp>().clientId(),
        ResponseTypeValues.CODE,
        Uri.parse(Constants.redirectUrl)
    ).setScopes(Constants.scope)
        .build()


    var authStatus by mutableStateOf(authStateManager.current.isAuthorized)
        private set

    fun updateUserDetails() {
        CoroutineScope(ioDispatcher).launch {
            authStateManager.getFreshToken(authService)
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
            ClientSecretPost(getApplication<WakaTimeApp>().clientSecret())
        ) { tokenResponse, authorizationException ->
            authorizationException?.let(Timber::e)
            viewModelScope.launch {
                authStateManager.updateAfterTokenResponse(tokenResponse, authorizationException)
                authStatus = authStateManager.current.isAuthorized
            }
        }
    }
}
