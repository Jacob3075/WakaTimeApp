package com.jacob.wakatimeapp.details.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.data.dtos.TotalTimeForProjectDTO
import com.jacob.wakatimeapp.details.domain.models.TotalProjectTime
import kotlinx.datetime.toLocalDate

@Suppress("MagicNumber")
fun TotalTimeForProjectDTO.toModel() = TotalProjectTime(
    totalTime = Time.fromDecimal(data.decimal.toFloat()),
    startDate = data.range.startDate.toLocalDate(),
    percentageCalculated = data.percentCalculated ?: 100,
    message = data.text,
)
