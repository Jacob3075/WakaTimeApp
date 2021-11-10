package com.jacob.wakatimeapp.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedStatsPage : Fragment() {
    private val viewModel by viewModels<DailyDetailsPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            WakaTimeAppTheme {
                DetailedStatsPageContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DetailedStatsPageContent(viewModel: DailyDetailsPageViewModel) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
    ) {
        Column {
            HeaderSection()
        }
    }
}

@Composable
private fun HeaderSection() {
}
