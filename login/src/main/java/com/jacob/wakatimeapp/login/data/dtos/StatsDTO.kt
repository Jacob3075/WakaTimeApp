package com.jacob.wakatimeapp.login.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatsDTO(
    val data: Data,
) {
    @Serializable
    data class Data(
        val categories: List<Category>,
        val dependencies: List<Dependency>,
        val editors: List<Editor>,
        val end: String,
        val holidays: Int,
        val id: String,
        @SerialName("is_already_updating") val isAlreadyUpdating: Boolean,
        @SerialName("is_including_today") val isIncludingToday: Boolean,
        @SerialName("is_other_usage_visible") val isOtherUsageVisible: Boolean,
        @SerialName("is_stuck") val isStuck: Boolean,
        @SerialName("is_up_to_date") val isUpToDate: Boolean,
        @SerialName("is_up_to_date_pending_future") val isUpToDatePendingFuture: Boolean,
        val languages: List<Language>,
        val machines: List<Machine>,
        @SerialName("modified_at") val modifiedAt: String,
        @SerialName("operating_systems") val operatingSystems: List<OperatingSystem>,
        @SerialName("percent_calculated") val percentCalculated: Int,
        val projects: List<Project>,
        val range: String,
        val start: String,
        val status: String,
        val timeout: Int,
        val timezone: String,
        @SerialName("total_seconds") val totalSeconds: Double,
        @SerialName("total_seconds_including_other_language") val totalSecondsIncludingOtherLanguage: Double,
        @SerialName("user_id") val userId: String,
        val username: String,
        @SerialName("writes_only") val writesOnly: Boolean,
    ) {
        @Serializable
        data class BestDay(
            @SerialName("created_at") val createdAt: String,
            val date: String,
            val id: String,
            @SerialName("modified_at") val modifiedAt: String,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double,
        )

        @Serializable
        data class Category(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double,
        )

        @Serializable
        data class Dependency(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double,
        )

        @Serializable
        data class Editor(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double,
        )

        @Serializable
        data class Language(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double,
        )

        @Serializable
        data class Machine(
            val decimal: String,
            val digital: String,
            val hours: Int,
            @SerialName("machine_name_id") val machineNameId: String,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double,
        )

        @Serializable
        data class OperatingSystem(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double,
        )

        @Serializable
        data class Project(
            val decimal: String,
            val digital: String,
            val hours: Int,
            val minutes: Int,
            val name: String,
            val percent: Double,
            val text: String,
            @SerialName("total_seconds") val totalSeconds: Double,
        )
    }
}
