package com.jacob.wakatimeapp.home.data.network.mappers

import com.jacob.wakatimeapp.home.data.network.dtos.CreateExtractResDTO
import com.jacob.wakatimeapp.home.domain.models.ExtractCreationProgress

internal fun CreateExtractResDTO.toModel() = ExtractCreationProgress(
    createdAt = data.createdAt,
    downloadUrl = data.downloadUrl,
    hasFailed = data.hasFailed,
    isProcessing = data.isProcessing,
    isStuck = data.isStuck,
    percentComplete = data.percentComplete,
    id = data.id,
    expires = data.expires,
    status = data.status,
    type = data.type,
)
