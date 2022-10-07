package com.jacob.wakatimeapp.home.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditorDTO(
    val decimal: String,
    val digital: String,
    val hours: Int,
    val minutes: Int,
    val name: String,
    val percent: Double,
    val seconds: Int,
    val text: String,
    @SerialName("total_seconds") val totalSeconds: Double,
)
