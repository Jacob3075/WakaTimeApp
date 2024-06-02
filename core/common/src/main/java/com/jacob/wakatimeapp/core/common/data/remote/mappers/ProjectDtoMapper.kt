package com.jacob.wakatimeapp.core.common.data.remote.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.remote.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.Project

fun ProjectDTO.toModel() = Project(
    time = Time(
        hours = hours,
        minutes = minutes,
        decimal = decimal.toFloat(),
    ),
    name = name,
    percent = percent,
)
