package com.jacob.wakatimeapp.search.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDetailsDTO
import com.jacob.wakatimeapp.search.domain.models.ProjectListWithPageNumber

fun ProjectDetailsDTO.toModel() = data.map { ProjectDetails(name = it.name) }

fun ProjectDetailsDTO.toModelWithPageNumber() = ProjectListWithPageNumber(
    projectList = this.toModel(),
    pageNumber = page,
    totalPageCount = totalPages,
)

data class ProjectDetails(
    val name: String,
)
