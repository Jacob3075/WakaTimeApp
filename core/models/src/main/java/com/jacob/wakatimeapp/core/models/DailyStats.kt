package com.jacob.wakatimeapp.core.models

import java.time.LocalDate

data class DailyStats(
    val timeSpent: com.jacob.wakatimeapp.core.models.Time,
    val projectsWorkedOn: List<Project>,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
    val date: LocalDate
)
