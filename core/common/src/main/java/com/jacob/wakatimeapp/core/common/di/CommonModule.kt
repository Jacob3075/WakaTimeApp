package com.jacob.wakatimeapp.core.common.di

import android.content.Context
import com.jacob.wakatimeapp.core.common.UserSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import net.openid.appauth.AuthorizationService

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Singleton
    @Provides
    fun provideUserSession() = UserSession()

    @Singleton
    @Provides
    fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO

    @Singleton
    @Provides
    fun provideAuthorizationService(@ApplicationContext context: Context) =
        AuthorizationService(context)
}
