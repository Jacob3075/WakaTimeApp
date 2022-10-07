package com.jacob.wakatimeapp.home.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetLast7DaysStatsResDTO(
    val data: List<Data>,
    val end: String,
    val start: String,
    @SerialName("cummulative_total") val cumulativeTotal: CumulativeTotal,
) {
    @Serializable
    data class CumulativeTotal(
        val decimal: String,
        val digital: String,
        val seconds: Double,
        val text: String,
    )

    @Serializable
    data class Data(
        val categories: List<CategoryDTO>,
        val dependencies: List<DependencyDTO>,
        val editors: List<EditorDTO>,
        val languages: List<LanguageDTO>,
        val machines: List<MachineDTO>,
        val projects: List<ProjectDTO>,
        val range: RangeDTO,
        @SerialName("operating_systems") val operatingSystems: List<OperatingSystemDTO>,
        @SerialName("grand_total") val grandTotal: GrandTotalDTO,
    )
}
