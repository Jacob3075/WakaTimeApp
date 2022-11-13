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
    val longestStreak: Streak,
    val currentStreak: Streak,
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
data class Streak(
    val start: LocalDate,
    val end: LocalDate,
) {
    val days: Int = if (this == ZERO) 0 else start.daysUntil(end) + 1

    operator fun plus(other: Streak) = when {
        this == ZERO -> other
        other == ZERO -> this
        this in other -> Streak(other.start, other.end)
        other in this -> Streak(start, end)
        other.start in padded() -> Streak(start, other.end)
        start in other.padded() -> Streak(other.start, end)
        else -> {
            Timber.e("Cannot add streaks $this and $other")
            ZERO
        }
    }

    operator fun contains(day: LocalDate) = day in start..end

    operator fun contains(other: Streak) = other.start in this && other.end in this

    operator fun compareTo(streak: Streak) = days.compareTo(streak.days)

    private fun padded() = Streak(start - oneDay, end + oneDay)

    /**
     * Checks if 2 streaks can be combined into 1
     *
     * NOTE: DOES NOT HANDLE CASE WHERE ONE OF THE STREAKS IS [ZERO], (breaks a test)
     */
    fun canBeCombinedWith(other: Streak) =
        this in other || other in this || other.start in padded() || start in other.padded()

    companion object {
        private val oneDay = DatePeriod(days = 1)

        val ZERO = Streak(
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
