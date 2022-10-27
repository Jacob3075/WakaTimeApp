package com.jacob.wakatimeapp.home.domain

import com.jacob.wakatimeapp.core.models.Time
import kotlinx.datetime.LocalDate

fun Map<LocalDate, Time>.getLatestStreakInRange() = toSortedMap()
    .entries
    .reversed()
    .takeWhile { it.value != Time.ZERO }
