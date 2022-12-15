package com.jacob.wakatimeapp.search.domain.models

import com.jacob.wakatimeapp.search.data.network.mappers.ProjectDetails

data class ProjectListWithPageNumber(
    val projectList: List<ProjectDetails>,
    val pageNumber: Int,
    val totalPageCount: Int,
)
