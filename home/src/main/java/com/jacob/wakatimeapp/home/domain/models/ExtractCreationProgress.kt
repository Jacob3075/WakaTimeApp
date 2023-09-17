package com.jacob.wakatimeapp.home.domain.models

internal data class ExtractCreationProgress(
    val createdAt: String,
    val downloadUrl: String,
    val hasFailed: Boolean,
    val isProcessing: Boolean,
    val isStuck: Boolean,
    val percentComplete: Double,
    val id: String,
    val expires: String,
    val status: String,
    val type: String,
)
