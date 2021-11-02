package com.jacob.wakatimeapp.home.data.mappers

import com.jacob.wakatimeapp.common.data.DtoMapper
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO.Data
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import com.jacob.wakatimeapp.home.domain.models.Project
import com.jacob.wakatimeapp.home.domain.models.StatsRange
import com.jacob.wakatimeapp.home.domain.models.WeeklyStats
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetWeeklyStatsResMapper @Inject constructor() :
    DtoMapper<GetLast7DaysStatsResDTO, WeeklyStats> {
    override fun fromDtoToModel(dto: GetLast7DaysStatsResDTO) = WeeklyStats(
        totalTime = Time.createFromDigitalStringFormat(dto.cumulativeTotal.digital),
        dailyStats = getDailyStatsFromDto(dto.data),
        range = StatsRange(
            LocalDate.parse(dto.start, DateTimeFormatter.ISO_DATE),
            LocalDate.parse(dto.end, DateTimeFormatter.ISO_DATE)
        )
    )

    private fun getDailyStatsFromDto(data: List<Data>) = data.map {
        DailyStats(
            Time.createFromDigitalStringFormat(it.grandTotal.digital),
            mostUsedEditor = "",
            mostUsedLanguage = "",
            mostUsedOs = "",
            date = LocalDate.parse(it.range.date),
            projectsWorkedOn = it.projects.map { project ->
                Project(
                    Time(
                        project.hours,
                        project.minutes
                    ),
                    project.name,
                    project.percent
                )
            }
        )
    }

    override fun fromModelToDto(model: WeeklyStats): GetLast7DaysStatsResDTO = TODO()
}
