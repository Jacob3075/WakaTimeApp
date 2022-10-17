package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.core.models.WeeklyStats
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.serialization.Serializable

@Serializable
data class Last7DaysStats(
    val timeSpentToday: Time,
    val projectsWorkedOn: List<Project>,
    val weeklyTimeSpent: Map<LocalDate, Time>,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
)

@Serializable
data class HomePageUserDetails(
    val fullName: String,
    val photoUrl: String,
)

@Serializable
data class Streaks(
    val currentStreak: StreakRange,
    val longestStreak: StreakRange,
)

@Serializable
data class StreakRange(
    val start: LocalDate,
    val end: LocalDate,
) {
    val days = start.daysUntil(end)
}

fun WeeklyStats.toLoadedStateData() = Last7DaysStats(
    timeSpentToday = todaysStats.timeSpent,
    projectsWorkedOn = todaysStats.projectsWorkedOn,
    weeklyTimeSpent = dailyStats.associate { it.date to it.timeSpent },
    mostUsedLanguage = todaysStats.mostUsedLanguage,
    mostUsedEditor = todaysStats.mostUsedEditor,
    mostUsedOs = todaysStats.mostUsedOs,
)

fun UserDetails.toHomePageUserDetails() = HomePageUserDetails(
    fullName = fullName,
    photoUrl = photoUrl
)
