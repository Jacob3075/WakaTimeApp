package com.jacob.wakatimeapp.search

import com.jacob.wakatimeapp.search.data.network.SearchProjectAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object SearchProjectModule {
    @Singleton
    @Provides
    internal fun provideSearchProjectAPI(retrofit: Retrofit) =
        retrofit.create(SearchProjectAPI::class.java)
}
