package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.jacob.wakatimeapp.core.ui.theme.assets.Assets
import com.jacob.wakatimeapp.core.ui.theme.assets.LocalAssets

@Composable
fun WakaTimeAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(
        LocalSpacing provides Spacing,
        LocalGradients provides Gradients,
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
