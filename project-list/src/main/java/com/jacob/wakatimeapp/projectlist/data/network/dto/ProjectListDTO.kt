package com.jacob.wakatimeapp.projectlist.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectListDTO(
    val data: List<ProjectDetailsDTO>,
    val page: Int,
    val total: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("next_page") val nextPage: Int,
    @SerialName("prev_page") val prevPage: Int,
)
