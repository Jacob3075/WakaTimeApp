package com.jacob.wakatimeapp.details.domain.models

import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import kotlinx.datetime.LocalDate

data class ProjectStats(
    val totalTime: Time,
    val dailyProjectStats: Map<LocalDate, Time>,
    val range: StatsRange,
    val languages: List<String>,
    val operatingSystems: List<String>,
    val editors: List<String>,
)
