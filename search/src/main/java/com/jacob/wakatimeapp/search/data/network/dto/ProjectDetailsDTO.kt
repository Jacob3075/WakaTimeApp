package com.jacob.wakatimeapp.search.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDetailsDTO(
    val id: String,
    val name: String,
    val repository: RepositoryDTO?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("human_readable_last_heartbeat_at") val humanReadableLastHeartbeatAt: String?,
    @SerialName("last_heartbeat_at") val lastHeartbeatAt: String?,
) {
    @Serializable
    data class RepositoryDTO(
        val description: String?,
        val homepage: String?,
        val name: String,
        @SerialName("star_count") val starCount: Int,
        @SerialName("default_branch") val defaultBranch: String,
        @SerialName("fork_count") val forkCount: Int,
        @SerialName("full_name") val fullName: String,
        @SerialName("html_url") val htmlUrl: String,
        @SerialName("wakatime_project_name") val wakatimeProjectName: String,
        @SerialName("urlencoded_name") val urlencodedName: String,
        @SerialName("modified_at") val modifiedAt: String,
    )
}
