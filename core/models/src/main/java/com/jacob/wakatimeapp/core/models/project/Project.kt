package com.jacob.wakatimeapp.core.models.project

import com.jacob.wakatimeapp.core.models.Time
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val time: Time,
    val name: String,
    val percent: Double,
)
