package com.jacob.wakatimeapp.home.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class RangeDTO(
    val date: String,
    val end: String,
    val start: String,
    val text: String,
    val timezone: String,
)
