package com.jacob.wakatimeapp.login.domain.models

import kotlinx.datetime.LocalDateTime

internal data class ExtractCreationProgress(
    val createdAt: LocalDateTime,
    val downloadUrl: String?,
    val hasFailed: Boolean,
    val isProcessing: Boolean,
    val isStuck: Boolean,
    val percentComplete: Float,
    val id: String,
    val expires: LocalDateTime?,
    val status: String,
    val type: String,
)
