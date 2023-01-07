package com.jacob.wakatimeapp.details.domain.models

import com.jacob.wakatimeapp.core.models.Time
import kotlinx.datetime.LocalDate

data class TotalProjectTime(
    val totalTime: Time,
    val startDate: LocalDate,
    val percentageCalculated: Int,
    val message: String,
)
