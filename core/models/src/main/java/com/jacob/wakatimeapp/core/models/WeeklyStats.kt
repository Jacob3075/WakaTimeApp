package com.jacob.wakatimeapp.core.models

data class WeeklyStats(
    val totalTime: com.jacob.wakatimeapp.core.models.Time,
    val dailyStats: List<DailyStats>,
    val range: StatsRange,
    val todaysStats: DailyStats = dailyStats.last(),
)
