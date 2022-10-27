package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.core.models.WeeklyStats
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import timber.log.Timber

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

    operator fun plus(other: StreakRange): StreakRange = when {
        end == other.start -> StreakRange(start, other.end)
        other.end == start -> StreakRange(other.start, end)
        end == other.start.minus(1, DateTimeUnit.DAY) -> StreakRange(start, other.end)
        start == other.end.plus(1, DateTimeUnit.DAY) -> StreakRange(other.start, end)
        start < other.start && end > other.end -> StreakRange(start, end)
        other.start < start && other.end > end -> StreakRange(other.start, other.end)
        start < other.start && end > other.start -> StreakRange(start, other.end)
        start < other.end && end > other.start -> StreakRange(other.start, end)
        else -> {
            Timber.e("Cannot add streaks $this and $other")
            ZERO
        }
    }

    companion object {
        val ZERO = StreakRange(
            Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()).date,
            Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()).date
        )
    }
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
