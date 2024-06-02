package com.jacob.wakatimeapp.core.common.data.remote.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDTO(
    val decimal: String,
    val digital: String,
    val hours: Int,
    val minutes: Int,
    val name: String,
    val percent: Double,
    val seconds: Int,
    val text: String,
    @SerialName("total_seconds") val totalSeconds: Double,
) {
    val isUnknownProject = name == "Unknown Project"
}
