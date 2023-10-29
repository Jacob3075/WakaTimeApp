package com.jacob.wakatimeapp.home.data.network.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AllTimeDataDTO(
    @SerialName("data") internal val data: Data,
    @SerialName("message") internal val message: String?,
) {
    @Serializable
    internal data class Data(
        internal val timeout: Int?,
        internal val decimal: Float?,
        internal val digital: String,
        internal val range: Range,
        internal val text: String,
        @SerialName("total_seconds") internal val totalSeconds: String,
        @SerialName("is_up_to_date") internal val isUpToDate: Boolean?,
        @SerialName("percent_calculated") internal val percentCalculated: Int?,
    )

    @Serializable
    internal data class Range(
        @SerialName("end") internal val end: String,
        @SerialName("end_date") internal val endDate: String,
        @SerialName("end_text") internal val endText: String,
        @SerialName("start") internal val start: String,
        @SerialName("start_date") internal val startDate: String,
        @SerialName("start_text") internal val startText: String,
        @SerialName("timezone") internal val timezone: String,
    )
}
