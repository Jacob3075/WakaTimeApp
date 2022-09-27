package com.jacob.wakatimeapp.home.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetLast7DaysStatsResDTO(
    val data: List<Data>,
    val end: String,
    val start: String,
    @SerialName("cummulative_total") val cumulativeTotal: CumulativeTotal
) {
    @Serializable
    data class CumulativeTotal(
        val decimal: String,
        val digital: String,
        val seconds: Double,
        val text: String
    )

    @Serializable
    data class Data(
        val categories: List<Category>,
        val dependencies: List<Dependency>,
        val editors: List<Editor>,
        val languages: List<Language>,
        val machines: List<Machine>,
        val projects: List<Project>,
        val range: Range,
        @SerialName("operating_systems") val operatingSystems: List<OperatingSystem>,
        @SerialName("grand_total") val grandTotal: GrandTotal
    ) {
        @Serializable
        data class Category(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val seconds: Int,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double
        )

        @Serializable
        data class Editor(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val seconds: Int,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double
        )

        @Serializable
        data class GrandTotal(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double
        )

        @Serializable
        data class Language(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val seconds: Int,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double
        )

        @Serializable
        data class Machine(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val seconds: Int,
            val text: String,
            @SerialName("machine_name_id") val machineNameId: String,
            @SerialName("total_seconds") val totalSeconds: Double
        )

        @Serializable
        data class OperatingSystem(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val seconds: Int,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double
        )

        @Serializable
        data class Project(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val seconds: Int,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double
        )

        @Serializable
        data class Range(
            val date: String,
            val end: String,
            val start: String,
            val text: String,
            val timezone: String
        )

        @Serializable
        class Dependency
    }
}
