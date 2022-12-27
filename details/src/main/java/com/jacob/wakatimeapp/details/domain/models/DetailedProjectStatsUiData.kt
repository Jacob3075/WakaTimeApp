package com.jacob.wakatimeapp.details.domain.models

import com.jacob.wakatimeapp.core.models.Time
import kotlinx.datetime.LocalDate

data class DetailedProjectStatsUiData(
    val totalProjectTime: TotalProjectTime,
    val averageTime: Time,
    val dailyProjectStats: Map<LocalDate, Time>,
    val languages: List<String>,
    val operatingSystems: List<String>,
    val editors: List<String>,
)
