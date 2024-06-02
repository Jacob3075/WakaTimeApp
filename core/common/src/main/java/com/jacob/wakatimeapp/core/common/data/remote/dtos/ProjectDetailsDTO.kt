package com.jacob.wakatimeapp.core.common.data.remote.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDetailsDTO(
    val data: List<DataDTO>,
    val page: Int,
    val total: Int,
    @SerialName("next_page") val nextPage: Int?,
    @SerialName("prev_page") val prevPage: Int?,
    @SerialName("total_pages") val totalPages: Int,
) {
    @Serializable
    data class DataDTO(
        val id: String,
        val name: String,
        val repository: RepositoryDTO?,
        val url: String,
        @SerialName("created_at") val createdAt: String,
        @SerialName("human_readable_last_heartbeat_at") val humanReadableLastHeartbeatAt: String?,
        @SerialName("last_heartbeat_at") val lastHeartbeatAt: String?,
    ) {
        @Serializable
        data class RepositoryDTO(
            val description: String?,
            val name: String,
            @SerialName("created_at") val createdAt: String,
            @SerialName("full_name") val fullName: String,
            @SerialName("html_url") val htmlUrl: String,
            @SerialName("last_synced_at") val lastSyncedAt: String?,
            @SerialName("modified_at") val modifiedAt: String,
        )
    }
}
