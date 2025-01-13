package com.jacob.wakatimeapp.details.ui

import com.jacob.wakatimeapp.core.common.utils.toDate
import com.jacob.wakatimeapp.core.models.Streak
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.domain.models.DetailedProjectStatsUiData
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

sealed class DetailsPageViewState {
    data object Loading : DetailsPageViewState()

    data class Loaded(
        val projectName: String,
        val todaysDate: LocalDate,
        val uiData: DetailedProjectStatsUiData,
    ) : DetailsPageViewState() {
        val statsForProject = uiData.dailyProjectStats
        private val filteredData = filterDayStatsToNonZeroDays()
        private val streaksInProject = Streak.getStreaksIn(statsForProject)

        val totalTime = filteredData.fold(Time.ZERO, Time::plus)
        val averageTime = Time.fromDecimal(totalTime.decimal.div(filteredData.size))
        val startDate = statsForProject.keys.minBy(LocalDate::toEpochDays)
        val numberOfDaysWorked = filteredData.filter { it != Time.ZERO }.size

        val currentStreakInProject = getCurrentStreak(streaksInProject)
        val longestStreakInProject = streaksInProject.maxByOrNull(Streak::days) ?: Streak.ZERO

        private val maxWorkedDay = statsForProject.maxByOrNull { it.value.totalSeconds }
        val dayMostWorked = maxWorkedDay?.key ?: Instant.DISTANT_PAST.toDate()
        val maxTimeWorkedInDay = maxWorkedDay?.value ?: Time.ZERO

        val mostUsedLang = uiData.languages.mostUsed
        val mostUsedEditor = uiData.editors.mostUsed
        val mostUsedMachine = uiData.machines.mostUsed
        val mostUsedOs = uiData.operatingSystems.mostUsed

        private fun filterDayStatsToNonZeroDays(): List<Time> {
            return statsForProject.values.filter { it != Time.ZERO }
        }

        private fun getCurrentStreak(streaksIn: List<Streak>) = streaksIn.maxByOrNull(Streak::end)
            ?.let { currentStreak ->
                return@let when (currentStreak.end) {
                    todaysDate -> currentStreak
                    else -> null
                }
            }
            ?: Streak.ZERO
    }

    data class Error(val error: com.jacob.wakatimeapp.core.models.Error) : DetailsPageViewState()
}
