package com.jacob.wakatimeapp.home.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.dtos.GetDailyStatsResDTO.Data
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun GetDailyStatsResDTO.toModel() = DailyStats(
    timeSpent = Time.createFrom(cumulativeTotal.digital, cumulativeTotal.decimal),
    projectsWorkedOn = getProjectsFromDto(data.first()),
    mostUsedLanguage = "",
    mostUsedEditor = "",
    mostUsedOs = "",
    date = LocalDate.parse(data.first().range.date, DateTimeFormatter.ISO_DATE)
)

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
