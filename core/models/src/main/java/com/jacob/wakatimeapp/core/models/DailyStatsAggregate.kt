package com.jacob.wakatimeapp.core.models

data class DailyStatsAggregate(val values: List<DailyStats>) {
    val totalTime = values.map(DailyStats::timeSpent).fold(Time.ZERO, Time::plus)

    val range = StatsRange(
        startDate = values.minOf(DailyStats::date),
        endDate = values.maxOf(DailyStats::date),
    )
}
