package com.jacob.wakatimeapp.core.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class StatsRange(
    val startDate: LocalDate,
    val endDate: LocalDate,
) {
    operator fun plus(other: StatsRange) = StatsRange(
        startDate = minOf(startDate, other.startDate),
        endDate = maxOf(endDate, other.endDate),
    )

    constructor(startDate: String, endDate: String) : this(
        startDate = startDate.takeWhile { it != 'T' }.toLocalDate(),
        endDate = endDate.takeWhile { it != 'T' }.toLocalDate(),
    )

    companion object {
        val ZERO = StatsRange(
            startDate = Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()).date,
            endDate = Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()).date,
        )
    }
}
