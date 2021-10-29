package com.jacob.wakatimeapp.home.data.mappers

import com.jacob.wakatimeapp.common.data.DtoMapper
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.home.data.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import javax.inject.Inject

class GetDailyStatsResMapper @Inject constructor() : DtoMapper<GetDailyStatsResDTO, DailyStats> {
    override fun fromDtoToModel(dto: GetDailyStatsResDTO) = DailyStats(
        timeSpent = Time.createFromDigitalStringFormat(dto.cumulativeTotal.digital),
        recentProjects = "",
        mostUsedLanguage = "",
        mostUsedEditor = "",
        mostUsedOs = ""
    )

    override fun fromModelToDto(model: DailyStats): GetDailyStatsResDTO {
        TODO("Not yet implemented")
    }
}
