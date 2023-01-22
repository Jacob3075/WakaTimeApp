package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun LanguagesTab(modifier: Modifier = Modifier) {
    Text("Languages", modifier = modifier.fillMaxSize())
}

@Composable
internal fun EditorsTab(modifier: Modifier = Modifier) {
    Text("Editors", modifier = modifier.fillMaxSize())
}

@Composable
internal fun OperatingSystemsTab(modifier: Modifier = Modifier) {
    Text("Operating Systems", modifier = modifier.fillMaxSize())
}
