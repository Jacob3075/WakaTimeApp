package com.jacob.wakatimeapp.common.utils

import android.content.Context
import net.openid.appauth.AuthorizationService

fun getFreshToken(context: Context): String? {
    val authStateManager = AuthStateManager.getInstance(context)
    val authService = AuthorizationService(context)

    if (authStateManager.current.needsTokenRefresh) {
        authStateManager.current.performActionWithFreshTokens(authService) { _, _, _ ->
            authStateManager.replace(authStateManager.current)
        }
    }

    return authStateManager.current.accessToken
}
