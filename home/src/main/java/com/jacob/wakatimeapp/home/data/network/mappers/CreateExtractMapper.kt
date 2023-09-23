package com.jacob.wakatimeapp.home.data.network.mappers

import com.jacob.wakatimeapp.core.common.utils.toDateTime
import com.jacob.wakatimeapp.home.data.network.dtos.CreateExtractResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.CreatedExtractResDTO
import com.jacob.wakatimeapp.home.domain.models.ExtractCreationProgress
import kotlinx.datetime.Instant

internal fun CreateExtractResDTO.toModel() = ExtractCreationProgress(
    createdAt = Instant.parse(data.createdAt).toDateTime(),
    downloadUrl = data.downloadUrl,
    hasFailed = data.hasFailed,
    isProcessing = data.isProcessing,
    isStuck = data.isStuck,
    percentComplete = data.percentComplete.toFloat() / 100,
    id = data.id,
    expires = data.expires?.let { Instant.parse(it).toDateTime() },
    status = data.status,
    type = data.type,
)

internal fun CreatedExtractResDTO.toModel() = data.map {
    ExtractCreationProgress(
        createdAt = Instant.parse(it.createdAt).toDateTime(),
        downloadUrl = it.downloadUrl,
        hasFailed = it.hasFailed,
        isProcessing = it.isProcessing,
        isStuck = it.isStuck,
        percentComplete = it.percentComplete.toFloat() / 100,
        id = it.id,
        expires = it.expires?.let { it1 -> Instant.parse(it1).toDateTime() },
        status = it.status,
        type = it.type,
    )
}
