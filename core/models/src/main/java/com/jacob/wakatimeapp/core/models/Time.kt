package com.jacob.wakatimeapp.core.models

data class Time(
    val hours: Int,
    val minutes: Int,
    val decimal: Float,
    val totalSeconds: Int = calculateTotalSeconds(hours, minutes),
) {
    fun toMinutes(): Int = (hours * MINUTES_IN_HOURS) + minutes

    fun formattedPrint() = "${hours}H, ${minutes}M"

    fun longFormattedPrint() = "$hours Hours, $minutes Minutes"

    companion object {
        private fun calculateTotalSeconds(hours: Int, minutes: Int) =
            (hours * MINUTES_IN_HOURS * MINUTES_IN_HOURS) + (minutes * MINUTES_IN_HOURS)

        fun fromDecimal(decimal: Float): Time {
            val hours = decimal.toInt()
            val minutesDecimal = (decimal - hours) * MINUTES_IN_HOURS
            val minutes = minutesDecimal.toInt()
            val totalSeconds = calculateTotalSeconds(hours, minutes)
            return Time(
                decimal = decimal,
                hours = hours,
                minutes = minutes,
                totalSeconds = totalSeconds
            )
        }

        fun createFrom(timeString: String, decimal: String): Time {
            val (hours, minutes) = timeString.split(":")
                .map(String::toInt)
            return Time(hours, minutes, decimal.toFloat())
        }

        private const val MINUTES_IN_HOURS = 60
    }
}
