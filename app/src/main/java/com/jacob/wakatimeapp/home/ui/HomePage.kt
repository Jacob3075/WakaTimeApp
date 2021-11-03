package com.jacob.wakatimeapp.home.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.jacob.wakatimeapp.R
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.common.models.UserDetails
import com.jacob.wakatimeapp.common.ui.OtherStatsCard
import com.jacob.wakatimeapp.common.ui.TimeSpentCard
import com.jacob.wakatimeapp.common.ui.theme.Colors
import com.jacob.wakatimeapp.common.ui.theme.Gradients
import com.jacob.wakatimeapp.common.ui.theme.Typography
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import com.jacob.wakatimeapp.home.ui.components.RecentProjects
import com.jacob.wakatimeapp.home.ui.components.WeeklyReport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomePage : Fragment() {
    private val viewModel by viewModels<HomePageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            WakaTimeAppTheme {
                HomePageContent(viewModel)
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
private fun HomePageContent(viewModel: HomePageViewModel) =
    Scaffold(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp)
                .verticalScroll(scrollState)
        ) {
            val userDetailsState by viewModel.userDetails.collectAsState(initial = null)
            val weeklyStatsFlow by viewModel.last7DaysStats.collectAsState()

            UserDetailsSection(userDetailsState)
            Spacer(modifier = Modifier.height(25.dp))
            TimeSpentSection(weeklyStatsFlow?.todaysStats)
            Spacer(modifier = Modifier.height(25.dp))
            RecentProjects(weeklyStatsFlow?.todaysStats)
            Spacer(modifier = Modifier.height(10.dp))
            WeeklyReport(weeklyStatsFlow?.dailyStats)
            Spacer(modifier = Modifier.height(25.dp))
            OtherDailyStats(weeklyStatsFlow?.todaysStats)
            Spacer(modifier = Modifier.height(25.dp))
        }
    }

@Composable
private fun UserDetailsSection(userDetails: UserDetails?) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = rememberImagePainter(
                data = userDetails?.photoUrl,
                builder = {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.place_holder)
                    fallback(R.drawable.place_holder)

                }
            ),
            contentDescription = "Profile image",
            modifier = Modifier.size(58.dp),
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = userDetails?.fullName ?: "",
            fontSize = Typography.h4.fontSize,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TimeSpentSection(dailyStats: DailyStats?) = TimeSpentCard(
    Gradients.primary,
    25,
    R.drawable.ic_time,
    "Total Time Spent Today",
    dailyStats?.timeSpent ?: Time(0, 0, 0f)
)

@Composable
private fun OtherDailyStats(dailyStats: DailyStats?) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Other Daily Stats", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Details", color = Colors.AccentText, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            gradient = Gradients.greenCyan,
            roundedCornerPercent = 25,
            iconId = R.drawable.ic_code_file,
            mainText = "Most Language Used",
            language = dailyStats?.mostUsedLanguage ?: ""
        )
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            gradient = Gradients.purpleCyanLight,
            roundedCornerPercent = 25,
            iconId = R.drawable.ic_laptop,
            mainText = "Most OS Used",
            language = dailyStats?.mostUsedOs ?: ""
        )
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            gradient = Gradients.purpleCyanDark,
            roundedCornerPercent = 25,
            iconId = R.drawable.ic_code,
            mainText = "Most Editor Used",
            language = dailyStats?.mostUsedEditor ?: ""
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun UserDetailsPreview() = WakaTimeAppTheme {
    UserDetailsSection(
        UserDetails(
            bio = "",
            email = "",
            id = "",
            timeout = 0,
            timezone = "",
            username = "",
            displayName = "",
            lastProject = "",
            fullName = "Jacob Bosco",
            durationsSliceBy = "",
            createdAt = "",
            dateFormat = "",
            photoUrl = ""
        )
    )
}
