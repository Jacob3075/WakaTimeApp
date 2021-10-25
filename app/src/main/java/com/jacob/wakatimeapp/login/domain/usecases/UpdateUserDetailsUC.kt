package com.jacob.wakatimeapp.login.domain.usecases

import com.jacob.wakatimeapp.common.models.UserSession
import com.jacob.wakatimeapp.login.data.LoginPageAPI
import com.jacob.wakatimeapp.login.data.mappers.UserDetailsMapper
import javax.inject.Inject

class UpdateUserDetailsUC @Inject constructor(
    private val loginPageAPI: LoginPageAPI,
    private val userSession: UserSession,
    private val userDetailsMapper: UserDetailsMapper,
) {
    suspend operator fun invoke(token: String) {
        val userDetailsResponse = loginPageAPI.getUserDetails(token)
        if (userDetailsResponse.isSuccessful) {
            val userDetails = userDetailsMapper.fromDtoToModel(userDetailsResponse.body()!!)
            userSession.logInUser(userDetails)
        }
    }
}
