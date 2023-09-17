package com.jacob.wakatimeapp.home.data.network.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateExtractReqDTO(
    internal val type: String,
)

@Serializable
internal data class CreateExtractResDTO(
    internal val data: Data,
) {
    @Serializable
    internal data class Data(
        @SerialName("created_at") internal val createdAt: String,
        @SerialName("download_url") internal val downloadUrl: String,
        @SerialName("has_failed") internal val hasFailed: Boolean,
        @SerialName("is_processing") internal val isProcessing: Boolean,
        @SerialName("is_stuck") internal val isStuck: Boolean,
        @SerialName("percent_complete") internal val percentComplete: Double,
        internal val id: String,
        internal val expires: String,
        internal val status: String,
        internal val type: String,
    )
}
