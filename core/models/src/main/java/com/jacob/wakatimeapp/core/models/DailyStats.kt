package com.jacob.wakatimeapp.core.models

import kotlinx.datetime.LocalDate

data class DailyStats(
    val timeSpent: Time,
    val projectsWorkedOn: List<Project>,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
    val date: LocalDate,
)
