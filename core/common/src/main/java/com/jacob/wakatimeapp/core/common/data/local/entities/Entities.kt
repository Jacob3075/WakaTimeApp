package com.jacob.wakatimeapp.core.common.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.Branch
import com.jacob.wakatimeapp.core.models.project.Machine
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.datetime.LocalDate

@Entity
data class DayEntity(
    @PrimaryKey(autoGenerate = false)
    val date: LocalDate,
    val grandTotal: Time,
    val editors: Editors,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val machines: List<Machine>,
)

@Entity(primaryKeys = ["name", "day"])
data class ProjectPerDay(
    val projectPerDayId: Long,
    val day: LocalDate,
    val name: String,
    val grandTotal: Time,
    val editors: Editors,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val branches: List<Branch>,
    val machines: List<Machine>,
)

data class DayWithProjects(
    val day: DayEntity,
    val projectsForDay: List<ProjectPerDay>,
)
