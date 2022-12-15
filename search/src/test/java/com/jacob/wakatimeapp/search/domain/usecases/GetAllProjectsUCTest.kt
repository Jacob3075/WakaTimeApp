package com.jacob.wakatimeapp.search.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.search.domain.usecases.GetAllProjectsUCRobot.Companion.emptyProjectListWithPageNumber
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetAllProjectsUCTest {
    private val robot = GetAllProjectsUCRobot()

    @Test
    internal fun `when there are no projects, then return an empty list`() = runTest {
        robot.build()
            .mockSearchProject(emptyProjectListWithPageNumber.right())
            .callUseCase()
            .resultShouldBeEmptyList()
    }
}
