package com.jacob.wakatimeapp.core.models.project

import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val time: Time,
    val name: String,
    val percent: Double,
)

data class ProjectDetails(val name: String, val time: Time, val range: Range)
