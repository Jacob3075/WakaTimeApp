package com.jacob.wakatimeapp.home.ui

import com.jacob.wakatimeapp.core.models.Error as CoreModelsError
import com.jacob.wakatimeapp.home.domain.models.HomePageUserDetails
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.Streaks

internal sealed class HomePageViewState {
    data class Loaded(
        val last7DaysStats: Last7DaysStats,
        val userDetails: HomePageUserDetails,
        val streaks: Streaks,
    ) : HomePageViewState()

    data class Error(val error: CoreModelsError) : HomePageViewState()

    object Loading : HomePageViewState()
}
