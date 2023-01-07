package com.jacob.wakatimeapp.core.common.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editor
import com.jacob.wakatimeapp.core.models.secondarystats.Language
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystem

fun LanguageDTO.toModel() = Language(
    name = name,
    time = Time.fromDecimal(decimal.toFloat()),
)

fun EditorDTO.toModel() = Editor(
    name = name,
    time = Time.fromDecimal(decimal.toFloat()),
)

fun OperatingSystemDTO.toModel() = OperatingSystem(
    name = name,
    time = Time.fromDecimal(decimal.toFloat()),
)
