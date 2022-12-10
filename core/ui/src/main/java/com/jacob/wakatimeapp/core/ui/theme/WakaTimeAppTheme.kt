@file:Suppress("UnusedReceiverParameter")

package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.jacob.wakatimeapp.core.ui.theme.assets.Assets
import com.jacob.wakatimeapp.core.ui.theme.assets.LocalAssets
import com.jacob.wakatimeapp.core.ui.theme.colors.LocalGradients
import com.jacob.wakatimeapp.core.ui.theme.colors.dark.DarkColors
import com.jacob.wakatimeapp.core.ui.theme.colors.dark.DarkGradients
import com.jacob.wakatimeapp.core.ui.theme.colors.light.LightColors
import com.jacob.wakatimeapp.core.ui.theme.colors.light.LightGradients

@Composable
fun WakaTimeAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    val gradients = if (darkTheme) DarkGradients else LightGradients

    CompositionLocalProvider(
        LocalSpacing provides Spacing,
        LocalGradients provides gradients,
        LocalAssets provides Assets,
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            shapes = Shapes,
            content = content,
        )
    }
}

val MaterialTheme.spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

val MaterialTheme.gradients
    @Composable
    @ReadOnlyComposable
    get() = LocalGradients.current

val MaterialTheme.assets
    @Composable
    @ReadOnlyComposable
    get() = LocalAssets.current
