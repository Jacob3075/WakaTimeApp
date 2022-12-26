package com.jacob.wakatimeapp.core.models

import kotlinx.datetime.LocalDate
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
        startDate = startDate.toLocalDateTime().date,
        endDate = endDate.toLocalDateTime().date,
    )
}
