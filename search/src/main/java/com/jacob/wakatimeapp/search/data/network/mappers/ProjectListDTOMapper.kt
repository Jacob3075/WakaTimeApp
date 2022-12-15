package com.jacob.wakatimeapp.search.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.search.data.network.dto.ProjectDetailsDTO
import com.jacob.wakatimeapp.search.data.network.dto.ProjectListDTO
import com.jacob.wakatimeapp.search.domain.models.ProjectListWithPageNumber

fun ProjectListDTO.toModel() = data.map(ProjectDetailsDTO::toModel)

fun ProjectListDTO.toModelWithPageNumber() = ProjectListWithPageNumber(
    projectList = data.map { it.toModel() },
    pageNumber = page,
    totalPageCount = totalPages,
)
