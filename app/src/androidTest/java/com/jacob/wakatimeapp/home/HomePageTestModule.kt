package com.jacob.wakatimeapp.home

import com.jacob.wakatimeapp.common.data.OfflineDataStore
import com.jacob.wakatimeapp.common.models.ErrorTypes
import com.jacob.wakatimeapp.common.models.Result
import com.jacob.wakatimeapp.common.models.UserDetails
import com.jacob.wakatimeapp.common.utils.Utils
import com.jacob.wakatimeapp.home.data.HomePageAPI
import com.jacob.wakatimeapp.home.domain.usecases.GetLast7DaysStatsUC
import com.jacob.wakatimeapp.home.ui.HomePageViewModel
import com.jacob.wakatimeapp.home.ui.HomePageViewState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomePageTestModule {
    @Singleton
    @Provides
    fun provideMockApi(): HomePageAPI = mockk()

//    @Singleton
//    @Provides
//    fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideMockOfflineDataStore(): OfflineDataStore {
        val mockOfflineDataStore = mockk<OfflineDataStore>()

        every { mockOfflineDataStore.getUserDetails(any()) } returns flowOf(UserDetails(
            bio = "",
            email = "",
            id = "",
            15,
            timezone = "",
            username = "",
            displayName = "",
            lastProject = "",
            fullName = "",
            durationsSliceBy = "",
            createdAt = "",
            dateFormat = "",
            photoUrl = ""
        ))

        return mockOfflineDataStore
    }

    @Singleton
    @Provides
    fun provideMockUtils(): Utils {
        val mockUtils = mockk<Utils>()

        every { mockUtils.getFreshToken(any()) } returns ""

        return mockUtils
    }

    @Singleton
    @Provides
    fun provideMockGetLast7DaysStatsUC(): GetLast7DaysStatsUC {
        val mockUseCase = mockk<GetLast7DaysStatsUC>()

        coEvery { mockUseCase("") } returns Result.Failure(ErrorTypes.NetworkError(Exception()))

        return mockUseCase
    }

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideMockHomePageViewModel(): HomePageViewModel {
        val mockViewModel = mockk<HomePageViewModel>(relaxed = true)

        every { mockViewModel.homePageState } returns MutableStateFlow(HomePageViewState.Loading).asStateFlow()

        return mockViewModel
    }

}
