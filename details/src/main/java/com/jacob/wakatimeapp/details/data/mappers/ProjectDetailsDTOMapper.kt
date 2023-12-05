package com.jacob.wakatimeapp.details.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDetailsDTO
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.ProjectDetails

fun ProjectDetailsDTO.toModel(): ProjectDetails {
    assert(data.size == 1) { "Expected only one project, but got ${data.size}" }
    return ProjectDetails(name = this.data.first().name, time = Time.ZERO)
}
