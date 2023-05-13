package com.jacob.wakatimeapp.home.data.network.dtos

import com.jacob.wakatimeapp.core.common.data.dtos.CategoryDTO
import com.jacob.wakatimeapp.core.common.data.dtos.CumulativeTotalDTO
import com.jacob.wakatimeapp.core.common.data.dtos.DependencyDTO
import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.GrandTotalDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.MachineDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.dtos.RangeDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetLast7DaysStatsResDTO(
    val data: List<Data>,
    val end: String,
    val start: String,
    val cumulativeTotal: CumulativeTotalDTO,
) {

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
