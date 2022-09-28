package com.jacob.wakatimeapp.login.usecases

import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.login.data.LoginPageAPI
import com.jacob.wakatimeapp.login.data.mappers.toModel
import javax.inject.Inject

class UpdateUserDetailsUC @Inject constructor(
    private val loginPageAPI: LoginPageAPI,
    private val authDataStore: AuthDataStore,
) {
    suspend operator fun invoke(token: String) {
        val userDetailsResponse = loginPageAPI.getUserDetails("Bearer $token")
        if (userDetailsResponse.isSuccessful) {
            val userDetails = userDetailsResponse.body()!!
                .toModel()
            authDataStore.updateUserDetails(userDetails)
        }
    }
}
