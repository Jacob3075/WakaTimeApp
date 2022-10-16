package com.jacob.wakatimeapp.login.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jacob.wakatimeapp.core.common.Constants
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.login.BuildConfig
import com.jacob.wakatimeapp.login.usecases.UpdateUserDetailsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest.Builder
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.ResponseTypeValues
import timber.log.Timber

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    application: Application,
    private val updateUserDetailsUC: UpdateUserDetailsUC,
    private val authTokenProvider: AuthTokenProvider,
) : AndroidViewModel(application) {
    private val authService = AuthorizationService(getApplication())
    private val _viewState: MutableStateFlow<LoginPageState> = MutableStateFlow(LoginPageState.Idle)
    val viewState: StateFlow<LoginPageState> = _viewState.asStateFlow()

    private val serviceConfig = AuthorizationServiceConfiguration(
        Uri.parse(Constants.authorizationUrl),
        Uri.parse(Constants.tokenUrl)
    )

    private val authRequest = Builder(
        serviceConfig,
        BuildConfig.CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(Constants.redirectUrl)
    ).setScopes(Constants.scope)
        .build()

    init {
        val authToken = authTokenProvider.current
        if (authToken.isAuthorized) _viewState.value = LoginPageState.Success
    }

    /**
     * Gets fresh auth token if needed and stores updates the stored user details
     *
     * Must be called from UI before navigating to the next screen so that a fresh
     * token is available
     */
    fun updateUserDetails() {
        viewModelScope.launch {
            authTokenProvider.getFreshToken()
                .filterNotNull()
                .first()
                .let { updateUserDetailsUC(it) }
        }
    }

    fun getAuthIntent(): Intent? {
        _viewState.value = LoginPageState.Loading
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    fun exchangeToken(intent: Intent) {
        val authorizationResponse = AuthorizationResponse.fromIntent(intent)
        if (authorizationResponse == null) {
            Timber.e("Empty auth intent")
            _viewState.value = LoginPageState.Error("Empty auth intent")
            return
        }

        authService.performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            ClientSecretPost(BuildConfig.CLIENT_SECRET)
        ) { tokenResponse, authorizationException ->
            authorizationException?.let(Timber::e)
            viewModelScope.launch {
                val authState = authTokenProvider.updateAfterTokenResponse(
                    tokenResponse,
                    authorizationException
                )
                if (authState.isAuthorized) {
                    _viewState.value = LoginPageState.Success
                } else {
                    _viewState.value = LoginPageState.Error("Failed to login")
                }
            }
        }
    }

    fun authDataNotFound(exceptionData: Intent) {
        val authorizationException = AuthorizationException.fromIntent(exceptionData)!!
        Timber.e("Data not present")
        Timber.e(authorizationException.toJsonString())
        _viewState.value = LoginPageState.Error(authorizationException.localizedMessage!!)
    }
}
