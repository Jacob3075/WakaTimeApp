package com.jacob.wakatimeapp.login.domain.usecases

import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.login.data.LoginPageAPI
import com.jacob.wakatimeapp.login.data.mappers.toModel
import com.jacob.wakatimeapp.login.ui.LoginPageState
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException
import timber.log.Timber

@Singleton
internal class UpdateUserDetailsUC @Inject constructor(
    private val loginPageAPI: LoginPageAPI,
    private val authDataStore: AuthDataStore,
) {
    suspend operator fun invoke(token: String) = try {
        val userDetailsResponse = loginPageAPI.getUserDetails("Bearer $token")
        if (userDetailsResponse.isSuccessful) {
            val userDetails = userDetailsResponse.body()!!.toModel()
            authDataStore.updateUserDetails(userDetails)
        }
        LoginPageState.Success
    } catch (e: HttpException) {
        Timber.e("HttpException: ${e.message}")
        LoginPageState.Error(e.message())
    } catch (exception: Exception) {
        Timber.e("Error updating user details: ${exception.message}")
        LoginPageState.Error("Error updating user details: ${exception.message}")
    }
}
