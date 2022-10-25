package com.jacob.wakatimeapp.core.models

data class Stats(
    val totalTime: Time,
    val dailyStats: List<DailyStats>,
    val range: StatsRange,
)
