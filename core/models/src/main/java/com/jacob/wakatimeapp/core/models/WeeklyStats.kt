package com.jacob.wakatimeapp.core.models

data class WeeklyStats(
    val totalTime: Time,
    val dailyStats: List<DailyStats>,
    val range: StatsRange,
    val todaysStats: DailyStats = dailyStats.last(),
)
