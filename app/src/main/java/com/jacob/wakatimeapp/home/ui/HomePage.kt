package com.jacob.wakatimeapp.home.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.jacob.wakatimeapp.R
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.common.ui.TimeSpentCard
import dagger.hilt.android.AndroidEntryPoint

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
                HomePageContent()
            }
        }
    }
}

@Composable
private fun HomePageContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .padding(all = 20.dp)
            .padding(top = 24.dp)) {
            UserDetails()
            TimeSpentCard()
            RecentProjects()
            WeeklyReport()
            OtherDailyStats()
        }
    }
}

@Composable
private fun UserDetails() {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = rememberImagePainter(
                data = "https://picsum.photos/200",
                builder = {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.place_holder)

                }
            ),
            contentDescription = "Profile image",
            modifier = Modifier.size(58.dp),
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = "Jacob Bosco",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
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


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun UserDetailsPreview() = WakaTimeAppTheme { UserDetails() }
