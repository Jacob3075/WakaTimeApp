package com.jacob.wakatimeapp.core.common.utils

import java.time.format.TextStyle.SHORT
import java.util.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toDate(timeZone: TimeZone = TimeZone.currentSystemDefault()) =
    toLocalDateTime(timeZone).date

fun LocalDate.getDisplayNameForDay(): String =
    dayOfWeek.getDisplayName(SHORT, Locale.getDefault())
