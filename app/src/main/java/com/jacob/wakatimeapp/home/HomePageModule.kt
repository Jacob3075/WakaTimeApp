package com.jacob.wakatimeapp.home

import com.jacob.wakatimeapp.home.data.HomePageAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomePageModule {
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): HomePageAPI =
        retrofit.create(HomePageAPI::class.java)
}