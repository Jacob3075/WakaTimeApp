package com.jacob.wakatimeapp.core.common.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GrandTotalDTO(
    val decimal: String,
    val digital: String,
    val hours: Int,
    val minutes: Int,
    val text: String,
    @SerialName("total_seconds") val totalSeconds: Long,
)
