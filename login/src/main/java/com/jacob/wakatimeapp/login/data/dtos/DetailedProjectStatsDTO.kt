package com.jacob.wakatimeapp.login.data.dtos

import com.jacob.wakatimeapp.core.common.data.remote.dtos.BranchDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.CategoryDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.CumulativeTotalDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.DependencyDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.EntityDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.GrandTotalDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.MachineDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.RangeDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedProjectStatsDTO(
    val branches: List<String>,
    val color: String?,
    val data: List<Data>,
    val end: String,
    val start: String,
    @SerialName("available_branches") val availableBranches: List<String>,
    @SerialName("cumulative_total") val cumulativeTotal: CumulativeTotalDTO,
) {
    @Serializable
    data class Data(
        val branches: List<BranchDTO>,
        val categories: List<CategoryDTO>,
        val dependencies: List<DependencyDTO>,
        val editors: List<EditorDTO>,
        val entities: List<EntityDTO>,
        val languages: List<LanguageDTO>,
        val machines: List<MachineDTO>,
        val range: RangeDTO,
        @SerialName("operating_systems") val operatingSystems: List<OperatingSystemDTO>,
        @SerialName("grand_total") val grandTotal: GrandTotalDTO,
    )
}
