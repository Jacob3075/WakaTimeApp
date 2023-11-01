package com.jacob.wakatimeapp.core.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate

data class DailyStats(
    val timeSpent: Time,
    val projectsWorkedOn: ImmutableList<Project>,
    val mostUsedLanguage: String,
    val mostUsedEditor: String,
    val mostUsedOs: String,
    val date: LocalDate,
) {
}
