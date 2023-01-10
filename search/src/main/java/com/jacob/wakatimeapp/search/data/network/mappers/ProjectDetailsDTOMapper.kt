package com.jacob.wakatimeapp.search.data.network.mappers

import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDetailsDTO
import com.jacob.wakatimeapp.search.domain.models.ProjectDetails
import com.jacob.wakatimeapp.search.domain.models.ProjectListWithPageNumber

fun ProjectDetailsDTO.toModel() = data.map { ProjectDetails(name = it.name) }

fun ProjectDetailsDTO.toModelWithPageNumber() = ProjectListWithPageNumber(
    projectList = this.toModel(),
    pageNumber = page,
    totalPageCount = totalPages,
)
