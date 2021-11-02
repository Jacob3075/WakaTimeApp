package com.jacob.wakatimeapp.home.data.mappers

import com.jacob.wakatimeapp.common.data.DtoMapper
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.home.data.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.dtos.GetDailyStatsResDTO.Data
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import com.jacob.wakatimeapp.home.domain.models.Project
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetDailyStatsResMapper @Inject constructor() : DtoMapper<GetDailyStatsResDTO, DailyStats> {
    override fun fromDtoToModel(dto: GetDailyStatsResDTO) = DailyStats(
        timeSpent = Time.createFromDigitalStringFormat(dto.cumulativeTotal.digital),
        projectsWorkedOn = getProjectsFromDto(dto.data.first()),
        mostUsedLanguage = "",
        mostUsedEditor = "",
        mostUsedOs = "",
        date = LocalDate.parse(dto.data.first().range.date, DateTimeFormatter.ISO_DATE)

    )

    override fun fromModelToDto(model: DailyStats): GetDailyStatsResDTO {
        TODO("Not yet implemented")
    }

    private fun getProjectsFromDto(data: Data) = data.run {
        projects.map { project ->
            Project(
                Time(
                    project.hours,
                    project.minutes
                ),
                project.name,
                project.percent
            )
        }
    }
}
