package com.jacob.wakatimeapp.home.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.dtos.EditorDTO
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO.Data
import com.jacob.wakatimeapp.home.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.home.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.home.data.dtos.ProjectDTO
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun GetLast7DaysStatsResDTO.toModel() = WeeklyStats(
    totalTime = Time.createFrom(cumulativeTotal.digital, cumulativeTotal.decimal),
    dailyStats = getDailyStatsFromDto(data),
    range = StatsRange(
        startDate = parseDate(start),
        endDate = parseDate(end)
    )
)

private fun getDailyStatsFromDto(data: List<Data>) = data.map {
    DailyStats(
        timeSpent = Time.createFrom(
            digialString = it.grandTotal.digital,
            decimal = it.grandTotal.decimal
        ),
        mostUsedEditor = it.editors.maxByOrNull(EditorDTO::percent)?.name ?: "NA",
        mostUsedLanguage = it.languages.maxByOrNull(LanguageDTO::percent)?.name ?: "NA",
        mostUsedOs = it.operatingSystems.maxByOrNull(OperatingSystemDTO::percent)?.name ?: "NA",
        date = LocalDate.parse(it.range.date),
        projectsWorkedOn = it.projects.filterNot(ProjectDTO::isUnknownProject)
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
