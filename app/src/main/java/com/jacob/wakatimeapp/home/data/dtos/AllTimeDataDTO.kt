package com.jacob.wakatimeapp.home.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllTimeDataDTO(
    @SerialName("data") val data: Data,
    @SerialName("message") val message: String?,
) {
    @Serializable
    data class Data(
        val timeout: Int?,
        val decimal: Float?,
        val digital: String,
        val range: Range,
        val text: String,
        @SerialName("total_seconds") val totalSeconds: String,
        @SerialName("is_up_to_date") val isUpToDate: Boolean?,
        @SerialName("percent_calculated") val percentCalculated: Int?,
    )

    @Serializable
    data class Range(
        @SerialName("end") val end: String,
        @SerialName("end_date") val endDate: String,
        @SerialName("end_text") val endText: String,
        @SerialName("start") val start: String,
        @SerialName("start_date") val startDate: String,
        @SerialName("start_text") val startText: String,
        @SerialName("timezone") val timezone: String,
    )
}
