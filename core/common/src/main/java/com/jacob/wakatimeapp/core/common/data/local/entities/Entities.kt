package com.jacob.wakatimeapp.core.common.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.jacob.wakatimeapp.core.models.Branch
import com.jacob.wakatimeapp.core.models.Machine
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.datetime.LocalDate

@Entity
data class DayEntity(
    @PrimaryKey(autoGenerate = true)
    val dayId: Long,
    val date: LocalDate,
    val grandTotal: Time,
    val editors: Editors,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val machines: List<Machine>,
)

@Entity
data class ProjectPerDay(
    @PrimaryKey(autoGenerate = true)
    val projectPerDayId: Long,
    val dayIdFk: Long,
    val name: String,
    val grandTotal: Time,
    val editors: Editors,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val branches: List<Branch>,
    val machines: List<Machine>,
)

data class DayWithStats(
    @Embedded val day: DayEntity,
    @Relation(
        parentColumn = "dayId",
        entityColumn = "dayIdFk",
    )
    val projectPerDay: ProjectPerDay,
)
