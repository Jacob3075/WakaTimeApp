package com.jacob.wakatimeapp.home.domain // ktlint-disable filename

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.models.Streak
import kotlinx.datetime.LocalDate

internal fun Map<LocalDate, Time>.getLatestStreakInRange() = toSortedMap()
    .entries
    .reversed()
    .takeWhile { it.value != Time.ZERO }
    .let {
        if (it.isEmpty()) Streak.ZERO else Streak(
            start = it.last().key,
            end = it.first().key,
        )
    }
