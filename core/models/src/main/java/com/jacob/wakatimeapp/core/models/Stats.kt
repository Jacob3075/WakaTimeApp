package com.jacob.wakatimeapp.core.models

@Deprecated("Too vague, totalTime and range properties can be derived from dailyStats property")
data class Stats(
    val totalTime: Time,
    val dailyStats: List<DailyStats>,
    val range: StatsRange,
)
