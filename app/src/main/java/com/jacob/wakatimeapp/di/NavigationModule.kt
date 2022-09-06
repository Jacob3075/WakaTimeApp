package com.jacob.wakatimeapp.di

import com.jacob.wakatimeapp.home.ui.HomePageDirections
import com.jacob.wakatimeapp.home.ui.HomePageNavigations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Singleton
    @Provides
    fun provideHomePageDirections(): HomePageNavigations = object : HomePageNavigations {
        override fun toDetailsPage() = HomePageDirections.homePageToDetailsPage()
    }
}
