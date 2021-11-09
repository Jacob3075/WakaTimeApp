package com.jacob.wakatimeapp.core.utils

import android.content.Context
import com.jacob.wakatimeapp.core.R

object Constants {
    fun clientId(context: Context) = context.getString(R.string.client_id)
    fun clientSecret(context: Context) = context.getString(R.string.client_secret)

    const val BASE_URL = "https://wakatime.com/"
    const val authorizationUrl = "https://wakatime.com/oauth/authorize"
    const val tokenUrl = "https://wakatime.com/oauth/token"
    const val redirectUrl = "wakatimeapp://oauth2redirect"
    const val scope = "email, read_logged_time, read_stats"
}
