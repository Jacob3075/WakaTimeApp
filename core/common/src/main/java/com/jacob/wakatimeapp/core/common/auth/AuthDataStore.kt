package com.jacob.wakatimeapp.core.common.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jacob.wakatimeapp.core.common.utils.log
import com.jacob.wakatimeapp.core.models.UserDetails
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthState

@Singleton
class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    val userDetails = dataStore.data.map { preferences ->
        preferences[KEY_USER_DETAILS]?.let<String, UserDetails>(json::decodeFromString)
    }
        .filterNotNull()
        .distinctUntilChanged()
        .log("userDetails")

    suspend fun updateUserDetails(userDetails: UserDetails) {
        dataStore.edit {
            it[KEY_USER_DETAILS] = json.encodeToString(userDetails)
        }
    }

    suspend fun getAuthState() = dataStore.data.map {
        it[KEY_AUTH_STATE]?.let(AuthState::jsonDeserialize) ?: AuthState()
    }
        .log("authState")
        .firstOrNull()
        ?: AuthState()

    suspend fun updateAuthState(newAuthState: AuthState?) {
        dataStore.edit {
            it[KEY_AUTH_STATE] = newAuthState?.jsonSerializeString().orEmpty()
        }
    }

    companion object {
        const val STORE_NAME = "userDetails"

        val KEY_USER_DETAILS = stringPreferencesKey("user_details")
        val KEY_AUTH_STATE = stringPreferencesKey("auth_state_string")
    }
}
