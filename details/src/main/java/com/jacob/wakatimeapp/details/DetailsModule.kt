package com.jacob.wakatimeapp.details

import com.jacob.wakatimeapp.details.data.ProjectDetailsPageAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailsModule {
    @Provides
    @Singleton
    internal fun provideProjectDetailsAPI(
        retrofit: retrofit2.Retrofit,
    ): ProjectDetailsPageAPI = retrofit.create(ProjectDetailsPageAPI::class.java)
}
