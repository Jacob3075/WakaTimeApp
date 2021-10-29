package com.jacob.wakatimeapp.common.models

data class Time(val hour: Int, val minutes: Int) {
    companion object {
        fun createFromDigitalStringFormat(timeString: String): Time {
            val (hours, minutes) = timeString.split(":").map { it.toInt() }
            return Time(hours, minutes)
        }
    }
}
