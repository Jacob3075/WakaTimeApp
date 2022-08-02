package com.jacob.wakatimeapp.core.network.mappers

import com.jacob.wakatimeapp.core.data.DtoMapper
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.core.network.dtos.GetDailyStatsResDTO.Data
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import com.jacob.wakatimeapp.home.domain.models.Project
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetDailyStatsResMapper @Inject constructor() : DtoMapper<GetDailyStatsResDTO, DailyStats> {
    override fun fromDtoToModel(dto: GetDailyStatsResDTO) = DailyStats(
        timeSpent = Time.createFrom(dto.cumulativeTotal.digital, dto.cumulativeTotal.decimal),
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
                    project.minutes,
                    project.decimal.toFloat()
                ),
                project.name,
                project.percent
            )
        }
    }
}
