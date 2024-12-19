package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.core.models.Streak
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.core.models.project.Project
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

data class HomePageUiData(
    val last7DaysStats: Last7DaysStats,
    val userDetails: HomePageUserDetails,
    val longestStreak: Streak,
    val currentStreak: Streak,
    val isStaleData: Boolean,
)

@Serializable
data class Last7DaysStats(
    val timeSpentToday: Time,
    val projectsWorkedOn: ImmutableList<Project>,
    val weeklyTimeSpent: ImmutableMap<LocalDate, Time>,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
) {
    val numberOfDaysWorked = weeklyTimeSpent.filter { it.value != Time.ZERO }.size
}

@Serializable
data class HomePageUserDetails(
    val fullName: String,
    val photoUrl: String,
)

fun WeeklyStats.toLoadedStateData() = Last7DaysStats(
    timeSpentToday = todaysStats.timeSpent,
    projectsWorkedOn = todaysStats.projectsWorkedOn,
    weeklyTimeSpent = dailyStats.associate { it.date to it.timeSpent }.toImmutableMap(),
    mostUsedLanguage = todaysStats.mostUsedLanguage,
    mostUsedEditor = todaysStats.mostUsedEditor,
    mostUsedOs = todaysStats.mostUsedOs,
)

fun UserDetails.toHomePageUserDetails() = HomePageUserDetails(
    fullName = fullName,
    photoUrl = photoUrl,
)
