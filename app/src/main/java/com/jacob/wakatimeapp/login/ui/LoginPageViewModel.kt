package com.jacob.wakatimeapp.login.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.jacob.wakatimeapp.WakaTimeApp
import com.jacob.wakatimeapp.core.utils.*
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserDetailsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
    private val utils: Utils,
) : AndroidViewModel(application) {
    private val authState = AuthStateManager.getInstance(getApplication())
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


    var authStatus by mutableStateOf(isLoggedIn())
        private set

    fun updateUserDetails() {
        CoroutineScope(ioDispatcher).launch {
            utils.getFreshToken(getApplication())?.let {
                updateUserDetailsUC(it, getApplication())
            }
        }
    }

//    fun exchangeToken(intent: Intent) {
//        authManager.exchangeToken(intent)
//        authStatus = authManager.isLoggedIn()
//        Timber.d(authStatus.toString())
//    }

    fun getAuthIntent(): Intent? = authService.getAuthorizationRequestIntent(authRequest)

    fun exchangeToken(intent: Intent) {
        AuthorizationResponse.fromIntent(intent)?.run {
            authService.performTokenRequest(
                createTokenExchangeRequest(),
                ClientSecretPost(getApplication<WakaTimeApp>().clientSecret())
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
