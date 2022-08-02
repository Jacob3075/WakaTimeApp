package com.jacob.wakatimeapp.core.models

import com.jacob.wakatimeapp.core.models.Time

data class Project(
    val time: com.jacob.wakatimeapp.core.models.Time,
    val name: String,
    val percent: Double,
)
