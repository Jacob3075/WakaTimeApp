package com.jacob.wakatimeapp.search.domain.models

data class ProjectListWithPageNumber(
    val projectList: List<ProjectDetails>,
    val pageNumber: Int,
    val totalPageCount: Int,
)
