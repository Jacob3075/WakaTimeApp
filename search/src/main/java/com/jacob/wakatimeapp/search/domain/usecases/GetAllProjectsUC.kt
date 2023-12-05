package com.jacob.wakatimeapp.search.domain.usecases

import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class GetAllProjectsUC @Inject constructor(
    private val wakaTimeAppDB: WakaTimeAppDB,
) {
    suspend operator fun invoke() = either {
        wakaTimeAppDB.getAllProjects().bind().sortedBy { it.range.endDate }.asReversed().toImmutableList()
    }
}
