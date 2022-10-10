package com.jacob.wakatimeapp.core.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class StatsRange(
    val startDate: LocalDate,
    val endDate: LocalDate,
)
