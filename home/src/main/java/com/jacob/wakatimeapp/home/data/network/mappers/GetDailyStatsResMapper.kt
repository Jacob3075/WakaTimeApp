package com.jacob.wakatimeapp.home.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.mappers.toModel
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.network.dtos.GetDailyStatsResDTO
import kotlinx.datetime.toLocalDate

fun GetDailyStatsResDTO.toModel() = DailyStats(
    timeSpent = Time.createFrom(cumulativeTotal.digital, cumulativeTotal.decimal),
    projectsWorkedOn = data.first().projects.map(ProjectDTO::toModel),
    mostUsedLanguage = "",
    mostUsedEditor = "",
    mostUsedOs = "",
    date = data.first().range.date.toLocalDate(),
)
