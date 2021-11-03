package com.jacob.wakatimeapp.common.models

data class Time(val hours: Int, val minutes: Int, val decimal: Float) {
    fun toMinutes(): Int = (hours * 60) + minutes

    companion object {
        fun createFrom(timeString: String, decimal: String): Time {
            val (hours, minutes) = timeString.split(":").map { it.toInt() }
            return Time(hours, minutes, decimal.toFloat())
        }
    }
}
