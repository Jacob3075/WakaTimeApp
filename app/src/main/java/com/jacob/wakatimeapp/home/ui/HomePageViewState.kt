package com.jacob.wakatimeapp.home.ui

import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.home.domain.models.WeeklyStats

sealed class HomePageViewState {
    data class Loaded(
        val contentData: WeeklyStats,
        val userDetails: UserDetails?,
    ) : HomePageViewState()

    data class Error(val errorMessage: String) : HomePageViewState()
    object Loading : HomePageViewState()
}
