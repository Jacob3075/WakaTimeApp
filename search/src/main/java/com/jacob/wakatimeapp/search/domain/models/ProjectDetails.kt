package com.jacob.wakatimeapp.search.domain.models

import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time

data class ProjectDetails(
    val name: String,
    val time: Time,
    val range: Range,
)
