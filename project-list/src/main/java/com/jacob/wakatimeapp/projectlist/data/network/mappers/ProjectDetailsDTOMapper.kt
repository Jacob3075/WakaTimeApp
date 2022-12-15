package com.jacob.wakatimeapp.projectlist.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.projectlist.data.network.dto.ProjectDetailsDTO

fun ProjectDetailsDTO.toModel() = ProjectDetails(name = this.name)

data class ProjectDetails(
    val name: String,
)
