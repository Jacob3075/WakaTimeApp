package com.jacob.wakatimeapp.core.common.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DependencyDTO(
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

@Serializable
data class LanguageDTO(
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

@Serializable
data class MachineDTO(
    val decimal: String,
    val digital: String,
    val hours: Int,
    val minutes: Int,
    val name: String,
    val percent: Double,
    val seconds: Int,
    val text: String,
    @SerialName("machine_name_id") val machineNameId: String,
    @SerialName("total_seconds") val totalSeconds: Double,
)

@Serializable
data class BranchDTO(
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

@Serializable
data class OperatingSystemDTO(
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

@Serializable
data class EntityDTO(
    val decimal: String,
    val digital: String,
    val hours: Int,
    val minutes: Int,
    val name: String,
    val percent: Double,
    val seconds: Int,
    val text: String,
    val type: String,
    @SerialName("total_seconds") val totalSeconds: Double,
)

@Serializable
data class CategoryDTO(
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
