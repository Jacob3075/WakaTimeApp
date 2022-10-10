package com.jacob.wakatimeapp.home

import com.jacob.wakatimeapp.home.data.network.HomePageAPI
import com.jacob.wakatimeapp.home.domain.DefaultInstantProvider
import com.jacob.wakatimeapp.home.domain.InstantProvider
import dagger.Binds
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
    fun provideHomePageService(retrofit: Retrofit): HomePageAPI =
        retrofit.create(HomePageAPI::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
interface HomePageModuleBinds {
    @Binds
    fun provideInstantProvider(impl: DefaultInstantProvider): InstantProvider
}
