package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.common.models.Time

data class WeeklyStats(
    val totalTime: Time,
    val dailyStats: List<DailyStats>,
    val range: StatsRange,
)
