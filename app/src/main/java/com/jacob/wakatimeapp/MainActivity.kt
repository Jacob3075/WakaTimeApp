package com.jacob.wakatimeapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.navigation.ApplicationNavigator
import com.jacob.wakatimeapp.navigation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContent {
            WakaTimeAppTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    dependenciesContainerBuilder = {
                        dependency(ApplicationNavigator(navController))
                    }
                )
            }
        }
    }
}
