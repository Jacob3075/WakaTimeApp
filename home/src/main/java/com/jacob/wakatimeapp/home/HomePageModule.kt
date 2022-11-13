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
abstract class HomePageModule {
    @Binds
    abstract fun provideInstantProvider(impl: DefaultInstantProvider): InstantProvider

    companion object {
        @Singleton
        @Provides
        internal fun provideHomePageService(retrofit: Retrofit) =
            retrofit.create(HomePageAPI::class.java)
    }
}
