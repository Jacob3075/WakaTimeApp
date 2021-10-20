package com.jacob.wakatimeapp.repositories

import timber.log.Timber

class WakaTimeRepository(
    private val wakaTimeAPI: WakaTimeAPI,
) {
    suspend fun getUserDetails(token: String) {
        val userDetails = wakaTimeAPI.getUserDetails(token)
        Timber.e(userDetails.toString())
    }
}