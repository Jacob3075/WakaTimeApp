@file:Suppress("TooManyFunctions")

package com.jacob.wakatimeapp.core.common.data.remote.mappers

import com.jacob.wakatimeapp.core.common.data.remote.dtos.BranchDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.MachineDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.Branch
import com.jacob.wakatimeapp.core.models.secondarystats.Editor
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Language
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.Machine
import com.jacob.wakatimeapp.core.models.secondarystats.Machines
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystem
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.collections.immutable.toImmutableList

fun List<LanguageDTO>.toModel() = map(LanguageDTO::toModel).let(::Languages)

fun List<EditorDTO>.toModel() = map(EditorDTO::toModel).let(::Editors)

fun List<OperatingSystemDTO>.toModel() = map(OperatingSystemDTO::toModel).let(::OperatingSystems)

fun List<MachineDTO>.toModel() = map(MachineDTO::toModel).let(::Machines)

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

fun MachineDTO.toModel() = Machine(
    name = name,
    time = Time.fromTotalSeconds(totalSeconds),
)

fun List<EditorDTO>.fromDto(): Editors {
    if (isEmpty()) return Editors(emptyList())
    return map(EditorDTO::toModel).let(::Editors)
}

fun List<LanguageDTO>.fromDto(): Languages {
    if (isEmpty()) return Languages(emptyList())
    return map(LanguageDTO::toModel).let(::Languages)
}

fun List<OperatingSystemDTO>.fromDto(): OperatingSystems {
    if (isEmpty()) return OperatingSystems(emptyList())
    return map(OperatingSystemDTO::toModel).let(::OperatingSystems)
}

fun List<MachineDTO>.fromDto(): Machines {
    if (isEmpty()) return Machines(emptyList())
    return map(MachineDTO::toModel).let(::Machines)
}

fun List<BranchDTO>.toBranch() = map { branch ->
    Branch(
        name = branch.name,
        time = Time.fromTotalSeconds(branch.totalSeconds),
    )
}
