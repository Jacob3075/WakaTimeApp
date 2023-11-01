package com.jacob.wakatimeapp.core.common.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CumulativeTotalDTO(
    val decimal: String,
    val digital: String,
    val seconds: Long,
    val text: String,
)
