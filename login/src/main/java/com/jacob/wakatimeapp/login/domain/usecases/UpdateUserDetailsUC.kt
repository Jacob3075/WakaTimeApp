package com.jacob.wakatimeapp.login.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.login.data.LoginPageAPI
import com.jacob.wakatimeapp.login.data.mappers.toModel
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException
import timber.log.Timber

@Singleton
internal class UpdateUserDetailsUC @Inject constructor(
    private val loginPageAPI: LoginPageAPI,
    private val authDataStore: AuthDataStore,
) {
    suspend operator fun invoke(token: String): Either<Error, Unit> = try {
        val userDetailsResponse = loginPageAPI.getUserDetails("Bearer $token")
        if (userDetailsResponse.isSuccessful) {
            val userDetails = userDetailsResponse.body()!!.toModel()
            authDataStore.updateUserDetails(userDetails)
        }
        Unit.right()
    } catch (exception: HttpException) {
        Timber.e("HttpException: ${exception.message}")
        Error.NetworkErrors.GenericError(exception.message()).left()
    } catch (exception: Exception) {
        Timber.e("Error updating user details: ${exception.message}")
        Error.NetworkErrors.GenericError(
            exception.message ?: "Could not get user details, check internet connection or login again",
        ).left()
    }
}
