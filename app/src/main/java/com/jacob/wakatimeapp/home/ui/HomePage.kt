package com.jacob.wakatimeapp.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.jacob.wakatimeapp.R
import com.jacob.wakatimeapp.common.models.Result
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.common.models.UserDetails
import com.jacob.wakatimeapp.common.ui.TimeSpentCard
import com.jacob.wakatimeapp.common.ui.theme.Gradients
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

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
private fun HomePageContent(viewModel: HomePageViewModel) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(all = 20.dp)
                .padding(top = 24.dp)
        ) {
            UserDetailsSection(viewModel.userDetails)
            Spacer(modifier = Modifier.height(22.dp))
            TimeSpentSection(viewModel.dailyStats)
            RecentProjects()
            WeeklyReport()
            OtherDailyStats()
        }
    }
}

@Composable
private fun TimeSpentSection(dailyStatsFlow: StateFlow<Result<DailyStats>>) {
    val dailyStats by dailyStatsFlow.collectAsState()

    when (dailyStats) {
        is Result.Failure -> return
        is Result.Empty -> {
            TimeSpentCard(
                Gradients.primary,
                25,
                R.drawable.ic_time,
                "Total Time Spent Today",
                Time(0, 0)
            )
        }
        is Result.Success -> TimeSpentCard(
            Gradients.primary,
            25,
            R.drawable.ic_time,
            "Total Time Spent Today",
            (dailyStats as Result.Success<DailyStats>).value.timeSpent
        )
    }
}

@Composable
private fun UserDetailsSection(userDetailsState: Flow<UserDetails?>) {
    val userDetails by userDetailsState.collectAsState(null)
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
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun WeeklyReport() {
}

@Composable
private fun OtherDailyStats() {
}

@Composable
private fun RecentProjects() {
}


//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun UserDetailsPreview() = WakaTimeAppTheme { UserDetailsSection() }
