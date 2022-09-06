package com.jacob.wakatimeapp

import androidx.navigation.NavDirections
import com.jacob.wakatimeapp.home.ui.HomePageDirections
import com.jacob.wakatimeapp.home.ui.HomePageNavigations
import com.jacob.wakatimeapp.login.ui.LoginPageDirections
import com.jacob.wakatimeapp.login.ui.LoginPageNavigations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Singleton
    @Provides
    fun provideHomePageDirections(): HomePageNavigations = object : HomePageNavigations {
        override fun toDetailsPage() = HomePageDirections.homePageToDetailsPage()
    }

    @Singleton
    @Provides
    fun provideLoginPageDirections(): LoginPageNavigations = object : LoginPageNavigations {
        override fun toHomePage(): NavDirections = LoginPageDirections.loginPageToHomePage()
    }
}
