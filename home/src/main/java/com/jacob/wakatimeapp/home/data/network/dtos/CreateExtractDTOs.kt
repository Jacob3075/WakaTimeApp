package com.jacob.wakatimeapp.home.data.network.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateExtractReqDTO(
    val type: String,
)

@Serializable
internal data class CreateExtractResDTO(
    val data: Data,
) {
    @Serializable
    internal data class Data(
        @SerialName("created_at") val createdAt: String,
        @SerialName("download_url") val downloadUrl: String?,
        @SerialName("has_failed") val hasFailed: Boolean,
        @SerialName("is_processing") val isProcessing: Boolean,
        @SerialName("is_stuck") val isStuck: Boolean,
        @SerialName("percent_complete") val percentComplete: Double,
        val id: String,
        val expires: String?,
        val status: String,
        val type: String,
    )
}
