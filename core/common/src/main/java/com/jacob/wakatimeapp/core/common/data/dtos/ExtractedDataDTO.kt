package com.jacob.wakatimeapp.core.common.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtractedDataDTO(
    val days: List<DayDTO>,
    val range: RangeDTO,
    val user: UserDTO,
) {
    @Serializable
    data class DayDTO(
        val categories: List<CategoryDTO>,
        val date: String,
        val dependencies: List<DependencyDTO>,
        val editors: List<EditorDTO>,
        val languages: List<LanguageDTO>,
        val machines: List<MachineDTO>,
        val projects: List<ProjectDTO>,
        @SerialName("grand_total") val grandTotal: GrandTotalDTO,
        @SerialName("operating_systems") val operatingSystems: List<OperatingSystemDTO>,
    ) {

        @Serializable
        data class ProjectDTO(
            val branches: List<BranchDTO>,
            val categories: List<CategoryDTO>,
            val dependencies: List<DependencyDTO>,
            val editors: List<EditorDTO>,
            val entities: List<EntityDTO>,
            val languages: List<LanguageDTO>,
            val machines: List<MachineDTO>,
            val name: String,
            @SerialName("grand_total") val grandTotal: GrandTotalDTO,
            @SerialName("operating_systems") val operatingSystems: List<OperatingSystemDTO>,
        ) {
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
                @SerialName("total_seconds") val totalSeconds: Long,
            )
        }
    }

    @Serializable
    data class RangeDTO(
        val end: Int,
        val start: Int,
    )

    @Serializable
    data class UserDTO(
        val bio: String?,
        val city: String?,
        val email: String?,
        val id: String,
        val location: String?,
        val photo: String?,
        val plan: String?,
        val timeout: Int?,
        val timezone: String?,
        val username: String,
        val website: String?,
        @SerialName("color_scheme") val colorScheme: String?,
        @SerialName("created_at") val createdAt: String?,
        @SerialName("date_format") val dateFormat: String?,
        @SerialName("default_dashboard_range") val defaultDashboardRange: String?,
        @SerialName("display_name") val displayName: String?,
        @SerialName("durations_slice_by") val durationsSliceBy: String?,
        @SerialName("full_name") val fullName: String?,
        @SerialName("github_username") val githubUsername: String?,
        @SerialName("has_basic_features") val hasBasicFeatures: Boolean,
        @SerialName("has_premium_features") val hasPremiumFeatures: Boolean,
        @SerialName("human_readable_website") val humanReadableWebsite: String?,
        @SerialName("invoice_id_format") val invoiceIdFormat: String?,
        @SerialName("is_email_confirmed") val isEmailConfirmed: Boolean,
        @SerialName("is_email_public") val isEmailPublic: Boolean,
        @SerialName("is_hireable") val isHireable: Boolean,
        @SerialName("is_onboarding_finished") val isOnboardingFinished: Boolean,
        @SerialName("languages_used_public") val languagesUsedPublic: Boolean,
        @SerialName("last_heartbeat_at") val lastHeartbeatAt: String?,
        @SerialName("last_plugin") val lastPlugin: String?,
        @SerialName("last_plugin_name") val lastPluginName: String?,
        @SerialName("last_project") val lastProject: String?,
        @SerialName("linkedin_username") val linkedinUsername: String?,
        @SerialName("logged_time_public") val loggedTimePublic: Boolean,
        @SerialName("meetings_over_coding") val meetingsOverCoding: Boolean,
        @SerialName("modified_at") val modifiedAt: String?,
        @SerialName("needs_payment_method") val needsPaymentMethod: Boolean,
        @SerialName("photo_public") val photoPublic: Boolean,
        @SerialName("profile_url") val profileUrl: String?,
        @SerialName("profile_url_escaped") val profileUrlEscaped: String?,
        @SerialName("public_email") val publicEmail: String?,
        @SerialName("public_profile_time_range") val publicProfileTimeRange: String?,
        @SerialName("share_all_time_badge") val shareAllTimeBadge: Boolean,
        @SerialName("share_last_year_days") val shareLastYearDays: String?,
        @SerialName("show_machine_name_ip") val showMachineNameIp: Boolean,
        @SerialName("time_format_24hr") val timeFormat24hr: Boolean,
        @SerialName("time_format_display") val timeFormatDisplay: String?,
        @SerialName("twitter_username") val twitterUsername: String?,
        @SerialName("weekday_start") val weekdayStart: Int,
        @SerialName("writes_only") val writesOnly: Boolean,
    )
}
