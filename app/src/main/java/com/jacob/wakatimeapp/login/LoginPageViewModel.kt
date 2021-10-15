package com.jacob.wakatimeapp.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.jacob.wakatimeapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor() : ViewModel() {

    val authRequest: AuthorizationRequest
    val authState = AuthState()

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
}