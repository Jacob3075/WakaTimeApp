package com.jacob.wakatimeapp.home.data.local.mappers

import com.jacob.wakatimeapp.home.data.local.entities.Last7DaysStatsEntity
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

fun Last7DaysStatsEntity.toModel() = Last7DaysStats(
    timeSpentToday = timeSpentToday,
    projectsWorkedOn = projectsWorkedOn.toImmutableList(),
    weeklyTimeSpent = weeklyTimeSpent.toImmutableMap(),
    mostUsedLanguage = mostUsedLanguage,
    mostUsedEditor = mostUsedEditor,
    mostUsedOs = mostUsedOs,
)

fun Last7DaysStats.toEntity() = Last7DaysStatsEntity(
    timeSpentToday = timeSpentToday,
    projectsWorkedOn = projectsWorkedOn.toList(),
    weeklyTimeSpent = weeklyTimeSpent.toMap(),
    mostUsedLanguage = mostUsedLanguage,
    mostUsedEditor = mostUsedEditor,
    mostUsedOs = mostUsedOs,
)
