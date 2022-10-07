package com.jacob.wakatimeapp.home.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.mappers.toModel
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.dtos.GetLast7DaysStatsResDTO.Data
import java.text.SimpleDateFormat
import java.time.LocalDate
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
            .map(ProjectDTO::toModel)
    )
}

private fun parseDate(dateTimeString: String) =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }
        .parse(dateTimeString)!!
