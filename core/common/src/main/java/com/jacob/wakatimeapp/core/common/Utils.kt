package com.jacob.wakatimeapp.core.common // ktlint-disable filename

import java.time.format.TextStyle.SHORT
import java.util.Locale
import kotlinx.datetime.LocalDate

fun LocalDate.getDisplayNameForDay(): String =
    dayOfWeek.getDisplayName(SHORT, Locale.getDefault())
