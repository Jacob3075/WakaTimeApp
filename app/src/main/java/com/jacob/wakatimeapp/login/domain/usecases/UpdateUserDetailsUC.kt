package com.jacob.wakatimeapp.login.domain.usecases

import android.content.Context
import com.jacob.wakatimeapp.common.data.OfflineDataStore
import com.jacob.wakatimeapp.login.data.LoginPageAPI
import com.jacob.wakatimeapp.login.data.mappers.UserDetailsMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

class UpdateUserDetailsUC @Inject constructor(
    private val loginPageAPI: LoginPageAPI,
    private val offlineDataStore: OfflineDataStore,
    private val userDetailsMapper: UserDetailsMapper,
) {
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(token: String, context: Context) {
        try {
            val userDetailsResponse = loginPageAPI.getUserDetails("Bearer $token")
            if (userDetailsResponse.isSuccessful) {
                val userDetails = userDetailsMapper.fromDtoToModel(userDetailsResponse.body()!!)
                offlineDataStore.updateUserDetails(context, userDetails)
            }
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }
}
