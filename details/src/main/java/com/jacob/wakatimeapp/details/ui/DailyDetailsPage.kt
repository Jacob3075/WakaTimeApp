package com.jacob.wakatimeapp.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyDetailsPage : Fragment() {
    private val viewModel by viewModels<DailyDetailsPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            WakaTimeAppTheme {
                Text(text = "Details Page")
            }
        }
    }
}
