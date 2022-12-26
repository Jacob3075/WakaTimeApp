package com.jacob.wakatimeapp.details.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TotalTimeForProjectDTO(
    val data: Data,
) {
    @Serializable
    data class Data(
        val decimal: String,
        val digital: String,
        val project: String,
        val range: Range,
        val text: String,
        val timeout: Int,
        @SerialName("is_up_to_date") val isUpToDate: Boolean,
        @SerialName("percent_calculated") val percentCalculated: Int,
        @SerialName("total_seconds") val totalSeconds: Double,
    ) {
        @Serializable
        data class Range(
            val timezone: String,
            @SerialName("start_date") val startDate: String,
            @SerialName("end_date") val endDate: String,
        )
    }
}
