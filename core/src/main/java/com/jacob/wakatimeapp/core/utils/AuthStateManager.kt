package com.jacob.wakatimeapp.core.utils

import com.jacob.wakatimeapp.core.data.OfflineDataStore
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenResponse
import javax.inject.Inject

/**
 * An example persistence mechanism for an [AuthState] instance.
 * This stores the instance in a shared preferences file, and provides thread-safe access and
 * mutation.
 *
 * [Source](https://github.com/openid/AppAuth-Android/blob/master/app/java/net/openid/appauthdemo/AuthStateManager.java)
 */
class AuthStateManager @Inject constructor(
    private val offlineDataStore: OfflineDataStore,
) {
    private val authStateFlow = offlineDataStore.getAuthState()

    suspend fun current(): AuthState = authStateFlow.lastOrNull() ?: AuthState()

    private suspend fun update(state: AuthState) = offlineDataStore.updateAuthState(state)

    suspend fun updateAfterTokenResponse(
        response: TokenResponse?,
        ex: AuthorizationException?,
    ): AuthState {
        val current = current()
        current.update(response, ex)
        update(current)
        return current
    }

    fun getFreshToken(authService: AuthorizationService) = callbackFlow {
        val current = current()

        if (!current.needsTokenRefresh) {
            send(current.accessToken)
            channel.close()
            return@callbackFlow
        }

        current.performActionWithFreshTokens(authService) { _, _, _ ->
            runBlocking {
                update(current)
                send(current.accessToken)
                channel.close()
            }
        }
    }
}
