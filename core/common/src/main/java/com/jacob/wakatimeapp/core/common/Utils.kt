package com.jacob.wakatimeapp.core.common

import java.time.format.TextStyle.SHORT
import java.util.Locale
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val LocalDate.Companion.today
    get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.getDisplayNameForDay(): String =
    dayOfWeek.getDisplayName(SHORT, Locale.getDefault())
