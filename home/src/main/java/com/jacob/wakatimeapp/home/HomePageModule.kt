package com.jacob.wakatimeapp.home

import com.jacob.wakatimeapp.home.data.network.HomePageAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object HomePageModule {
    @Singleton
    @Provides
    internal fun provideHomePageService(retrofit: Retrofit) =
        retrofit.create(HomePageAPI::class.java)
}
