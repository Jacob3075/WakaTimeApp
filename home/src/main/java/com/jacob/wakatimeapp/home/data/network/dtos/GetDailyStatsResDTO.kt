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
internal data class GetDailyStatsResDTO(
    internal val data: List<Data>,
    internal val end: String,
    internal val start: String,
    @SerialName("cumulative_total") internal val cumulativeTotal: CumulativeTotalDTO,
) {
    @Serializable
    internal data class Data(
        internal val categories: List<CategoryDTO>,
        internal val dependencies: List<DependencyDTO>,
        internal val editors: List<EditorDTO>,
        internal val languages: List<LanguageDTO>,
        internal val machines: List<MachineDTO>,
        internal val projects: List<ProjectDTO>,
        internal val range: RangeDTO,
        @SerialName("grand_total") internal val grandTotal: GrandTotalDTO,
        @SerialName("operating_systems") internal val operatingSystems: List<OperatingSystemDTO>,
    )
}
