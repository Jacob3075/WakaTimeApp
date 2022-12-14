package com.jacob.wakatimeapp.projectlist

import com.jacob.wakatimeapp.projectlist.data.network.ProjectListAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ProjectListModule {
    @Singleton
    @Provides
    internal fun provideProjectListAPI(retrofit: Retrofit) =
        retrofit.create(ProjectListAPI::class.java)
}
