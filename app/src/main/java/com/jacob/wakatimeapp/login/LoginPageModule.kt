package com.jacob.wakatimeapp.login

import com.jacob.wakatimeapp.login.data.LoginPageAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object LoginPageModule {
    @Singleton
    @Provides
    fun provideLoginPageService(retrofit: Retrofit): LoginPageAPI =
        retrofit.create(LoginPageAPI::class.java)

    @Singleton
    @Provides
    fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO
}
