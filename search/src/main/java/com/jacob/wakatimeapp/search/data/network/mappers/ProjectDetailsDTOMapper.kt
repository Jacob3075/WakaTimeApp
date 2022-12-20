package com.jacob.wakatimeapp.search.data.network.mappers // ktlint-disable filename

import androidx.compose.runtime.Stable
import com.jacob.wakatimeapp.search.data.network.dto.ProjectDetailsDTO

fun ProjectDetailsDTO.toModel() = ProjectDetails(name = this.name)

@Stable
data class ProjectDetails(
    val name: String,
)
