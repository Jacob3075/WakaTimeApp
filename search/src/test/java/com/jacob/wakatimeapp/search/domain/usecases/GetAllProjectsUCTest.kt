package com.jacob.wakatimeapp.search.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.search.domain.models.ProjectListWithPageNumber
import com.jacob.wakatimeapp.search.domain.usecases.GetAllProjectsUCRobot.Companion.emptyProjectListWithPageNumber
import com.jacob.wakatimeapp.search.domain.usecases.GetAllProjectsUCRobot.Companion.projectDetails
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

    @Test
    internal fun `when there is one page in the list, then only one request is made`() = runTest {
        robot.build()
            .mockSearchProject(
                data = ProjectListWithPageNumber(
                    projectList = List(20) { projectDetails },
                    pageNumber = 1,
                    totalPageCount = 1,
                ).right(),
                page = 1,
            )
            .callUseCase()
            .resultShouldBe(List(20) { projectDetails }.right())
            .verifySearchProject(page = 1)
    }

    @Test
    internal fun `when there are multiple pages of content, then multiple requests are made`() =
        runTest {
            robot.build()
                .mockSearchProject(
                    data = ProjectListWithPageNumber(
                        projectList = List(20) { projectDetails },
                        pageNumber = 1,
                        totalPageCount = 2,
                    ).right(),
                    page = 1,
                )
                .mockSearchProject(
                    data = ProjectListWithPageNumber(
                        projectList = List(20) { projectDetails },
                        pageNumber = 2,
                        totalPageCount = 2,
                    ).right(),
                    page = 2,
                )
                .callUseCase()
                .resultShouldBe(List(40) { projectDetails }.right())
                .verifySearchProject(page = 1)
                .verifySearchProject(page = 2)
        }
}
