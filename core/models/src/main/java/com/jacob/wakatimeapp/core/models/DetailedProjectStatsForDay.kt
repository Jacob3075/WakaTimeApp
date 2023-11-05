package com.jacob.wakatimeapp.core.models

import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.datetime.LocalDate

data class DetailedProjectStatsForDay(
    val name: String,
    val date: LocalDate,
    val totalTime: Time,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val editors: Editors,
    val branches: List<Branch>,
    val machines: List<Machine>,
)
