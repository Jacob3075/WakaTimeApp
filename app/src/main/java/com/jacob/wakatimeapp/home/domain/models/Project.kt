package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.core.models.Time

data class Project(
    val time: Time,
    val name: String,
    val percent: Double,
)
