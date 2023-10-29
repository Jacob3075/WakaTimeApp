package com.jacob.wakatimeapp.home.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.mappers.toModel
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.data.network.dtos.GetStatsForRangeResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetStatsForRangeResDTO.Data
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.toLocalDate

internal fun GetStatsForRangeResDTO.toModel() = DailyStatsAggregate(
    values = getDailyStatsFromDto(data),
)

private fun getDailyStatsFromDto(data: List<Data>) = data.map {
    DailyStats(
        timeSpent = Time.createFrom(
            digitalString = it.grandTotal.digital,
            decimal = it.grandTotal.decimal,
        ),
        mostUsedEditor = it.editors.maxByOrNull(EditorDTO::percent)?.name ?: "NA",
        mostUsedLanguage = it.languages.maxByOrNull(LanguageDTO::percent)?.name ?: "NA",
        mostUsedOs = it.operatingSystems.maxByOrNull(OperatingSystemDTO::percent)?.name ?: "NA",
        date = it.range.date.toLocalDate(),
        projectsWorkedOn = it.projects
            .filterNot(ProjectDTO::isUnknownProject)
            .map(ProjectDTO::toModel)
            .toImmutableList(),
    )
}
