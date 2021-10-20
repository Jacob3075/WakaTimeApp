package com.jacob.data.di

import com.jacob.data.models.UserSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {
    @Singleton
    @Provides
    fun provideUserSession() = UserSession()
}