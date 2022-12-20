package com.jacob.wakatimeapp.search.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.search.data.network.dto.ProjectDetailsDTO

fun ProjectDetailsDTO.toModel() = ProjectDetails(name = this.name)

data class ProjectDetails(
    val name: String,
)
