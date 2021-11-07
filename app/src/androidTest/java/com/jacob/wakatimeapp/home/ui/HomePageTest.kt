package com.jacob.wakatimeapp.home.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.Navigation.findNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.jacob.wakatimeapp.MainActivity
import com.jacob.wakatimeapp.R
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.home.HomePageModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(HomePageModule::class)
class HomePageTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var viewModel: HomePageViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activityRule.scenario.onActivity {
            findNavController(it, R.id.nav_host_fragment).navigate(R.id.homePage)
        }
//        composeRule.setContent {
//            WakaTimeAppTheme {
//                HomePageContent(viewModel = viewModel)
//            }
//        }
    }

    @Test
    fun name() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.jacob.wakatimeapp", appContext.packageName)

        composeRule.onNodeWithTag(HomePageTestTags.LOADING_ANIMATION_ILLUSTRATION).assertIsDisplayed()
        composeRule.onNodeWithTag(HomePageTestTags.LOADING_TEXT).assertIsDisplayed()
    }
}
