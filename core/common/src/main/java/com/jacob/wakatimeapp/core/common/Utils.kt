package com.jacob.wakatimeapp.core.common

import java.time.LocalDate
import java.time.format.TextStyle.SHORT
import java.util.*

fun LocalDate.getDisplayNameForDay(): String =
    dayOfWeek.getDisplayName(SHORT, Locale.getDefault())
