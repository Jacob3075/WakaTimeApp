package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.core.models.WeeklyStats
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import timber.log.Timber

data class CachedHomePageUiData(
    val last7DaysStats: Last7DaysStats,
    val userDetails: HomePageUserDetails,
    val streaks: Streaks,
    val isStaleData: Boolean,
)

@Serializable
data class Last7DaysStats(
    val timeSpentToday: Time,
    val projectsWorkedOn: List<Project>,
    val weeklyTimeSpent: Map<LocalDate, Time>,
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
    val days: Int = if (this == ZERO) 0 else start.daysUntil(end) + 1

    operator fun plus(other: StreakRange) = when {
        this == ZERO -> other
        other == ZERO -> this
        this in other -> StreakRange(other.start, other.end)
        other in this -> StreakRange(start, end)
        other.start paddedIn this -> StreakRange(start, other.end)
        start paddedIn other -> StreakRange(other.start, end)
        else -> {
            Timber.e("Cannot add streaks $this and $other")
            ZERO
        }
    }

    operator fun contains(day: LocalDate) = day in start..end

    operator fun contains(other: StreakRange) = other.start in this && other.end in this

    operator fun compareTo(streakRange: StreakRange) = days.compareTo(streakRange.days)

    private infix fun LocalDate.paddedIn(streakRange: StreakRange) =
        this in (streakRange.start - oneDay)..(streakRange.end + oneDay)

    fun canBeCombinedWith(other: StreakRange) =
        !(this == ZERO || other == ZERO || (this + other) == ZERO)

    companion object {
        private val oneDay = DatePeriod(days = 1)

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
