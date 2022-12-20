package com.jacob.wakatimeapp.home.data.local.entities

import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Last7DaysStatsEntity(
    val timeSpentToday: Time,
    val projectsWorkedOn: List<Project>,
    val weeklyTimeSpent: Map<LocalDate, Time>,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
)
