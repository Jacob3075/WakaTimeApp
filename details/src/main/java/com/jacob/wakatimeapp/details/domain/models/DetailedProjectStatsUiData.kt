package com.jacob.wakatimeapp.details.domain.models

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.datetime.LocalDate

data class DetailedProjectStatsUiData(
    val totalTime: Time,
    val averageTime: Time,
    val dailyProjectStats: ImmutableMap<LocalDate, Time>,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val editors: Editors,
)
