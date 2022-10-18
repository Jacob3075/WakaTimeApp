package com.jacob.wakatimeapp.core.common // ktlint-disable filename

import java.time.format.TextStyle.SHORT
import java.util.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val LocalDate.Companion.today
    get() = Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.getDisplayNameForDay(): String =
    dayOfWeek.getDisplayName(SHORT, Locale.getDefault())
