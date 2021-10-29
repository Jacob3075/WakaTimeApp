package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.common.models.Time

data class DailyStats(
    val timeSpent: Time,
    val recentProjects: String,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
)
