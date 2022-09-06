package com.jacob.wakatimeapp.login.usecases

import com.jacob.wakatimeapp.core.data.OfflineDataStore
import com.jacob.wakatimeapp.login.data.LoginPageAPI
import com.jacob.wakatimeapp.login.data.mappers.UserDetailsMapper
import javax.inject.Inject

class UpdateUserDetailsUC @Inject constructor(
    private val loginPageAPI: LoginPageAPI,
    private val offlineDataStore: OfflineDataStore,
    private val userDetailsMapper: UserDetailsMapper,
) {
    suspend operator fun invoke(token: String) {
        val userDetailsResponse = loginPageAPI.getUserDetails("Bearer $token")
        if (userDetailsResponse.isSuccessful) {
            val userDetails = userDetailsMapper.fromDtoToModel(userDetailsResponse.body()!!)
            offlineDataStore.updateUserDetails(userDetails)
        }
    }
}
