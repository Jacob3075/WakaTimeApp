package com.jacob.wakatimeapp.login.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserDetailsResDTO(
    val data: Data,
) {
    @Serializable
    data class Data(
        val bio: String,
        val email: String,
        val id: String,
        val timeout: Int,
        val timezone: String,
        val username: String,
        @SerialName("display_name") val displayName: String,
        @SerialName("last_project") val lastProject: String,
        @SerialName("full_name") val fullName: String,
        @SerialName("durations_slice_by") val durationsSliceBy: String,
        @SerialName("created_at") val createdAt: String,
        @SerialName("date_format") val dateFormat: String,
        @SerialName("photo") val photoUrl: String,
    )
}