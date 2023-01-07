package com.jacob.wakatimeapp.core.common.di

import android.content.Context
import com.jacob.wakatimeapp.core.common.utils.DefaultInstantProvider
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import dagger.Binds
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
@Suppress("UnnecessaryAbstractClass")
abstract class CommonModule {

    @Binds
    abstract fun provideInstantProvider(impl: DefaultInstantProvider): InstantProvider

    companion object {

        @Singleton
        @Provides
        @Suppress("InjectDispatcher")
        fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO

        @Singleton
        @Provides
        fun provideAuthorizationService(@ApplicationContext context: Context) =
            AuthorizationService(context)
    }
}
