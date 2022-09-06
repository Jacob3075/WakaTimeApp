package com.jacob.wakatimeapp.home.ui

import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.core.models.WeeklyStats

sealed class HomePageViewState {
    data class Loaded(
        val contentData: com.jacob.wakatimeapp.core.models.WeeklyStats,
        val userDetails: com.jacob.wakatimeapp.core.models.UserDetails?,
    ) : HomePageViewState()

    data class Error(val errorMessage: String) : HomePageViewState()
    object Loading : HomePageViewState()
}
