package com.jacob.wakatimeapp.home.data.mappers

import com.jacob.wakatimeapp.core.data.DtoMapper
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO.Data
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import com.jacob.wakatimeapp.home.domain.models.Project
import com.jacob.wakatimeapp.home.domain.models.StatsRange
import com.jacob.wakatimeapp.home.domain.models.WeeklyStats
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class GetWeeklyStatsResMapper @Inject constructor() :
    DtoMapper<GetLast7DaysStatsResDTO, WeeklyStats> {
    override fun fromDtoToModel(dto: GetLast7DaysStatsResDTO) = WeeklyStats(
        totalTime = Time.createFrom(dto.cumulativeTotal.digital, dto.cumulativeTotal.decimal),
        dailyStats = getDailyStatsFromDto(dto.data),
        range = StatsRange(
            startDate = parseDate(dto.start),
            endDate = parseDate(dto.end)
        )
    )

    private fun getDailyStatsFromDto(data: List<Data>) = data.map {
        DailyStats(
            timeSpent = Time.createFrom(it.grandTotal.digital, it.grandTotal.decimal),
            mostUsedEditor = it.editors.maxByOrNull { editor -> editor.percent }?.name ?: "NA",
            mostUsedLanguage = it.languages.maxByOrNull { editor -> editor.percent }?.name ?: "NA",
            mostUsedOs = it.operatingSystems.maxByOrNull { editor -> editor.percent }?.name ?: "NA",
            date = LocalDate.parse(it.range.date),
            projectsWorkedOn = it.projects.filterNot { project -> project.name == "Unknown Project" }
                .map { project ->
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
        )
    }

    private fun parseDate(dateTimeString: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        return sdf.parse(dateTimeString)!!
    }

    override fun fromModelToDto(model: WeeklyStats): GetLast7DaysStatsResDTO = TODO()
}
