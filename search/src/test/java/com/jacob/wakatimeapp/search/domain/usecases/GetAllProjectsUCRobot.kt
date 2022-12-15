package com.jacob.wakatimeapp.search.domain.usecases

import arrow.core.Either
import arrow.core.Either.Right
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.search.data.network.SearchProjectNetworkData
import com.jacob.wakatimeapp.search.data.network.mappers.ProjectDetails
import com.jacob.wakatimeapp.search.domain.models.ProjectListWithPageNumber
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk

internal class GetAllProjectsUCRobot {
    private lateinit var useCase: GetAllProjectsUC
    private val mockSearchProjectNetworkData: SearchProjectNetworkData = mockk()

    private var result: Either<Error, List<ProjectDetails>>? = null

    fun build() = apply {
        clearMocks(mockSearchProjectNetworkData)
        result = null

        useCase = GetAllProjectsUC(mockSearchProjectNetworkData)
    }

    suspend fun callUseCase() = apply {
        result = useCase()
    }

    fun resultShouldBeEmptyList() {
        result shouldBe Right(emptyList())
    }

    fun mockSearchProject(data: Either<Error, ProjectListWithPageNumber>, page: Int = 1) = apply {
        coEvery { mockSearchProjectNetworkData.getProjects(page) } returns data
    }

    companion object {
        val emptyProjectListWithPageNumber = ProjectListWithPageNumber(
            emptyList(),
            1,
            1,
        )
    }
}
