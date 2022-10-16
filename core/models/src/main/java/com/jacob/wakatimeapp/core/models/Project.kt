package com.jacob.wakatimeapp.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val time: Time,
    val name: String,
    val percent: Double,
)
