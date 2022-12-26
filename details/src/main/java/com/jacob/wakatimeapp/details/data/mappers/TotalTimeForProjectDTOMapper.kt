package com.jacob.wakatimeapp.details.data.mappers

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.data.dtos.TotalTimeForProjectDTO
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

fun TotalTimeForProjectDTO.toModel() = TotalProjectTime(
    totalTime = Time.fromDecimal(data.decimal.toFloat()),
    startDate = data.range.startDate.toLocalDate(),
    percentageCalculated = data.percentCalculated,
    message = data.text,
)

data class TotalProjectTime(
    val totalTime: Time,
    val startDate: LocalDate,
    val percentageCalculated: Int,
    val message: String,
)
