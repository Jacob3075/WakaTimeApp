package com.jacob.wakatimeapp.core.models

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
import kotlin.collections.Map.Entry

@Serializable
data class Streak(
    val start: LocalDate,
    val end: LocalDate,
) : Comparable<Streak> {
    val days: Int = if (this == ZERO) 0 else start.daysUntil(end) + 1

    operator fun plus(other: Streak) = when {
        this == ZERO -> other
        other == ZERO -> this
        this in other -> other
        other in this -> this
        other.start in padded() -> Streak(start, other.end)
        start in other.padded() -> Streak(other.start, end)
        else -> {
            Timber.e("Cannot add streaks $this and $other")
            ZERO
        }
    }

    operator fun contains(day: LocalDate) = day in start..end

    operator fun contains(other: Streak) = other.start in this && other.end in this

    override operator fun compareTo(other: Streak) = days.compareTo(other.days)

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
            Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()).date,
        )

        fun getStreaksIn(statsForDay: Map<LocalDate, Time>): List<Streak> = statsForDay.entries
            .groupConsecutive()
            .filter(List<Entry<LocalDate, Time>>::isNotEmpty)
            .toStreaks()

        /**
         * Given a list of days, groups consecutive days with non-zero stats into a list and adds that to a list
         *
         * [Source](https://stackoverflow.com/a/65357359/13181948)
         */
        private fun Iterable<Entry<LocalDate, Time>>.groupConsecutive() =
            fold(mutableListOf(mutableListOf<Entry<LocalDate, Time>>())) { groups, dateTimeEntry ->
                when (dateTimeEntry.value) {
                    Time.ZERO -> groups.add(mutableListOf())
                    else -> groups.last().add(dateTimeEntry)
                }
                groups
            }

        private fun List<List<Entry<LocalDate, Time>>>.toStreaks() = map {
            Streak(it.first().key, it.last().key)
        }
    }
}
