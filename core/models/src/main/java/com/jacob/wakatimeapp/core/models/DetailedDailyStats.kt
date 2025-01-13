package com.jacob.wakatimeapp.core.models

import com.jacob.wakatimeapp.core.models.project.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.Machines
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate

data class DetailedDailyStats(
    val date: LocalDate,
    val projects: ImmutableList<DetailedProjectStatsForDay>,
    val languages: Languages,
    val editors: Editors,
    val operatingSystems: OperatingSystems,
    val machines: Machines,
    val timeSpent: Time,
)
