package com.jacob.wakatimeapp.search.data

import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.search.domain.models.ProjectDetails

internal fun List<ProjectPerDay>.toProjectDetails() = groupBy(ProjectPerDay::name)
    .map {
        it.value
            .fold(
                ProjectDetails(
                    name = it.key,
                    time = Time.ZERO,
                    range = Range(
                        startDate = it.value.minOf(ProjectPerDay::day),
                        endDate = it.value.maxOf(ProjectPerDay::day),
                    ),
                ),
            ) { acc, projectPerDay ->
                acc.copy(time = acc.time + projectPerDay.grandTotal)
            }
    }
