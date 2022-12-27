package com.jacob.wakatimeapp.details.domain.models

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.datetime.LocalDate

data class DetailedProjectStatsUiData(
    val totalProjectTime: TotalProjectTime,
    val averageTime: Time,
    val dailyProjectStats: Map<LocalDate, Time>,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val editors: Editors,
)
