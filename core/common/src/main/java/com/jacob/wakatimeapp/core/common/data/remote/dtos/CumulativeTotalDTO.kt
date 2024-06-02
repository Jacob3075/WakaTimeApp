package com.jacob.wakatimeapp.core.common.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CumulativeTotalDTO(
    val decimal: String,
    val digital: String,
    val seconds: Double,
    val text: String,
)
