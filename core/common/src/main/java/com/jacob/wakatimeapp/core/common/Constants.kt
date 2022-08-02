package com.jacob.wakatimeapp.core.common

import android.content.Context
import com.jacob.wakatimeapp.core.R.string

object Constants {
    const val BASE_URL = "https://wakatime.com/"
    const val authorizationUrl = "https://wakatime.com/oauth/authorize"
    const val tokenUrl = "https://wakatime.com/oauth/token"
    const val redirectUrl = "wakatimeapp://oauth2redirect"
    const val scope = "email, read_logged_time, read_stats"
}

fun Context.clientId() = getString(string.client_id)
fun Context.clientSecret() = getString(string.client_secret)
