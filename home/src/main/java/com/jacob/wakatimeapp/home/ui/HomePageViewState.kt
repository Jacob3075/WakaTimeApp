package com.jacob.wakatimeapp.home.ui

import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

sealed class HomePageViewState {
    data class Loaded(
        val contentData: LoadedStateData,
        val userDetails: UserDetails?,
    ) : HomePageViewState()

    data class Error(val errorMessage: String) : HomePageViewState()
    object Loading : HomePageViewState()
}

@Serializable
data class LoadedStateData(
    val timeSpentToday: Time,
    val projectsWorkedOn: List<Project>,
    val weeklyTimeSpent: Map<LocalDate, Time>,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
) {
    init {
        require(weeklyTimeSpent.size == NUMBER_OF_DAYS) { "Weekly time spent must have 7 days" }
    }

    companion object {
        private const val NUMBER_OF_DAYS = 7
    }
}
