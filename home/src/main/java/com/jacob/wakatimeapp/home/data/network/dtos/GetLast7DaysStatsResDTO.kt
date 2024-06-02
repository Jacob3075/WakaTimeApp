package com.jacob.wakatimeapp.home.data.network.dtos

import com.jacob.wakatimeapp.core.common.data.remote.dtos.CategoryDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.CumulativeTotalDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.DependencyDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.GrandTotalDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.MachineDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.RangeDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetLast7DaysStatsResDTO(
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
        @SerialName("operating_systems") internal val operatingSystems: List<OperatingSystemDTO>,
        @SerialName("grand_total") internal val grandTotal: GrandTotalDTO,
    )
}
