package com.jacob.wakatimeapp.home.domain // ktlint-disable filename

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import kotlinx.datetime.LocalDate

internal fun Map<LocalDate, Time>.getLatestStreakInRange() = toSortedMap()
    .entries
    .reversed()
    .takeWhile { it.value != Time.ZERO }
    .let {
        if (it.isEmpty()) StreakRange.ZERO else StreakRange(
            start = it.last().key,
            end = it.first().key,
        )
    }
