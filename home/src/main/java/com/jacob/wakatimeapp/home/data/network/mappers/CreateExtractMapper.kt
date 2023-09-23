package com.jacob.wakatimeapp.home.data.network.mappers

import com.jacob.wakatimeapp.home.data.network.dtos.CreateExtractResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.CreatedExtractResDTO
import com.jacob.wakatimeapp.home.domain.models.ExtractCreationProgress

internal fun CreateExtractResDTO.toModel() = ExtractCreationProgress(
    createdAt = data.createdAt,
    downloadUrl = data.downloadUrl,
    hasFailed = data.hasFailed,
    isProcessing = data.isProcessing,
    isStuck = data.isStuck,
    percentComplete = data.percentComplete.toFloat() / 100,
    id = data.id,
    expires = data.expires,
    status = data.status,
    type = data.type,
)

internal fun CreatedExtractResDTO.toModel() = data.map {
    ExtractCreationProgress(
        createdAt = it.createdAt,
        downloadUrl = it.downloadUrl,
        hasFailed = it.hasFailed,
        isProcessing = it.isProcessing,
        isStuck = it.isStuck,
        percentComplete = it.percentComplete.toFloat() / 100,
        id = it.id,
        expires = it.expires,
        status = it.status,
        type = it.type,
    )
}
