package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class HomePageUiData(
    val timeSpentToday: Time,
    val projectsWorkedOn: List<Project>,
    val weeklyTimeSpent: Map<LocalDate, Time>,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
) {
    companion object {
        private const val NUMBER_OF_DAYS = 7
    }
}

fun WeeklyStats.toLoadedStateData() = HomePageUiData(
    timeSpentToday = totalTime,
    projectsWorkedOn = todaysStats.projectsWorkedOn,
    weeklyTimeSpent = dailyStats.associate { it.date to it.timeSpent },
    mostUsedLanguage = todaysStats.mostUsedLanguage,
    mostUsedEditor = todaysStats.mostUsedEditor,
    mostUsedOs = todaysStats.mostUsedOs,
)
