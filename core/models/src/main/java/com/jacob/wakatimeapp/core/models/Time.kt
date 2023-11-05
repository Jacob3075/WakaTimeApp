package com.jacob.wakatimeapp.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Time(
    val hours: Int,
    val minutes: Int,
    val decimal: Float,
    val totalSeconds: Double = calculateTotalSeconds(hours.toLong(), minutes.toLong()),
) {
    fun toMinutes(): Int = (hours * MINUTES_IN_HOURS) + minutes

    fun formattedPrint() = "${hours}H, ${minutes}M"

    fun longFormattedPrint() = "$hours Hours, $minutes Minutes"

    operator fun plus(other: Time) = fromTotalSeconds(totalSeconds + other.totalSeconds)

    companion object {
        val ZERO = Time(0, 0, 0f)

        fun fromTotalSeconds(totalSeconds: Double): Time {
            val hours = totalSeconds / (SECONDS_IN_MINUTES * MINUTES_IN_HOURS)
            val minutes = (totalSeconds % (SECONDS_IN_MINUTES * MINUTES_IN_HOURS)) / MINUTES_IN_HOURS
            val decimal = hours + (minutes.toFloat() / MINUTES_IN_HOURS)
            return Time(
                decimal = decimal.toFloat(),
                hours = hours.toInt(),
                minutes = minutes.toInt(),
                totalSeconds = totalSeconds,
            )
        }

        fun fromDecimal(decimal: Float): Time {
            val hours = decimal.toInt()
            val minutesDecimal = (decimal - hours) * MINUTES_IN_HOURS
            val minutes = minutesDecimal.toInt()
            val totalSeconds = calculateTotalSeconds(hours.toLong(), minutes.toLong())
            return Time(
                decimal = decimal,
                hours = hours,
                minutes = minutes,
                totalSeconds = totalSeconds,
            )
        }

        fun createFrom(digitalString: String, decimal: String): Time {
            val (hours, minutes) = digitalString.split(":")
                .map(String::toInt)
            return Time(hours, minutes, decimal.toFloat())
        }

        private fun calculateTotalSeconds(hours: Long, minutes: Long): Double =
            (hours * MINUTES_IN_HOURS * SECONDS_IN_MINUTES) + (minutes * SECONDS_IN_MINUTES).toDouble()

        private const val MINUTES_IN_HOURS = 60
        private const val SECONDS_IN_MINUTES = 60
    }
}
