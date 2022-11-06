@file:Suppress("UnusedReceiverParameter")

package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.jacob.wakatimeapp.core.ui.theme.assets.Assets
import com.jacob.wakatimeapp.core.ui.theme.assets.LocalAssets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradients
import com.jacob.wakatimeapp.core.ui.theme.colors.LocalGradients
import com.jacob.wakatimeapp.core.ui.theme.colors.LocalNewGradients
import com.jacob.wakatimeapp.core.ui.theme.colors.dark.DarkColors
import com.jacob.wakatimeapp.core.ui.theme.colors.dark.DarkGradients
import com.jacob.wakatimeapp.core.ui.theme.colors.dark.DarkNewGradients
import com.jacob.wakatimeapp.core.ui.theme.colors.light.LightColors
import com.jacob.wakatimeapp.core.ui.theme.colors.light.LightGradients
import com.jacob.wakatimeapp.core.ui.theme.colors.light.LightNewGradients

@Composable
fun WakaTimeAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    val gradients = if (darkTheme) DarkGradients else LightGradients
    val newGradients = if (darkTheme) DarkNewGradients else LightNewGradients

    CompositionLocalProvider(
        LocalSpacing provides Spacing,
        LocalGradients provides gradients,
        LocalNewGradients provides newGradients,
        LocalAssets provides Assets,
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

val MaterialTheme.gradients: Gradients
    @Composable
    @ReadOnlyComposable
    get() = LocalGradients.current

val MaterialTheme.assets: Assets
    @Composable
    @ReadOnlyComposable
    get() = LocalAssets.current
