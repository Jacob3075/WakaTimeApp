@file:Suppress("TooManyFunctions")

package com.jacob.wakatimeapp.core.common.data.mappers

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO.DayDTO.ProjectDTO.BranchDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.MachineDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.models.Branch
import com.jacob.wakatimeapp.core.models.Machine
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editor
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Language
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystem
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.collections.immutable.toImmutableList

fun List<LanguageDTO>.toModel() = map(LanguageDTO::toModel).let(::Languages)

fun List<EditorDTO>.toModel() = map(EditorDTO::toModel).let(::Editors)

fun List<OperatingSystemDTO>.toModel() = map(OperatingSystemDTO::toModel).let(::OperatingSystems)

fun List<ProjectDTO>.toModel() = filterNot(ProjectDTO::isUnknownProject)
    .map(ProjectDTO::toModel)
    .toImmutableList()

fun LanguageDTO.toModel() = Language(
    name = name,
    time = Time.fromTotalSeconds(totalSeconds),
)

fun EditorDTO.toModel() = Editor(
    name = name,
    time = Time.fromTotalSeconds(totalSeconds),
)

fun OperatingSystemDTO.toModel() = OperatingSystem(
    name = name,
    time = Time.fromTotalSeconds(totalSeconds),
)

fun List<EditorDTO>.fromDto() = map(EditorDTO::toModel).let(::Editors)

fun List<LanguageDTO>.fromDto() = map(LanguageDTO::toModel).let(::Languages)

fun List<OperatingSystemDTO>.fromDto() = map(OperatingSystemDTO::toModel).let(::OperatingSystems)

fun List<BranchDTO>.toModel() = map { branch ->
    Branch(
        name = branch.name,
        time = Time.fromTotalSeconds(branch.totalSeconds),
    )
}

fun List<MachineDTO>.toModel() = map { machine ->
    Machine(
        name = machine.name,
        time = Time.fromTotalSeconds(machine.totalSeconds),
    )
}
