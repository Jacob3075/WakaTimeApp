@file:Suppress("MagicNumber")

package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val lightPrimary = Color(0xFF006874)
private val lightOnPrimary = Color(0xFFFFFFFF)
private val lightPrimaryContainer = Color(0xFF96F0FF)
private val lightOnPrimaryContainer = Color(0xFF001F24)
private val lightSecondary = Color(0xFF4A6267)
private val lightOnSecondary = Color(0xFFFFFFFF)
private val lightSecondaryContainer = Color(0xFFCDE7EC)
private val lightOnSecondaryContainer = Color(0xFF051F23)
private val lightTertiary = Color(0xFF525E7D)
private val lightOnTertiary = Color(0xFFFFFFFF)
private val lightTertiaryContainer = Color(0xFFDAE2FF)
private val lightOnTertiaryContainer = Color(0xFF0E1B37)
private val lightError = Color(0xFFBA1A1A)
private val lightErrorContainer = Color(0xFFFFDAD6)
private val lightOnError = Color(0xFFFFFFFF)
private val lightOnErrorContainer = Color(0xFF410002)
private val lightBackground = Color(0xFFFAFDFD)
private val lightOnBackground = Color(0xFF191C1D)
private val lightSurface = Color(0xFFFAFDFD)
private val lightOnSurface = Color(0xFF191C1D)
private val lightSurfaceVariant = Color(0xFFDBE4E6)
private val lightOnSurfaceVariant = Color(0xFF3F484A)
private val lightOutline = Color(0xFF6F797A)
private val lightInverseOnSurface = Color(0xFFEFF1F1)
private val lightInverseSurface = Color(0xFF2E3132)
private val lightInversePrimary = Color(0xFF4FD8EB)
private val lightShadow = Color(0xFF000000)
private val lightSurfaceTint = Color(0xFF006874)

private val darkPrimary = Color(0xFF4FD8EB)
private val darkOnPrimary = Color(0xFF00363D)
private val darkPrimaryContainer = Color(0xFF004F57)
private val darkOnPrimaryContainer = Color(0xFF96F0FF)
private val darkSecondary = Color(0xFFB1CBD0)
private val darkOnSecondary = Color(0xFF1C3438)
private val darkSecondaryContainer = Color(0xFF334B4F)
private val darkOnSecondaryContainer = Color(0xFFCDE7EC)
private val darkTertiary = Color(0xFFBAC6EA)
private val darkOnTertiary = Color(0xFF24304D)
private val darkTertiaryContainer = Color(0xFF3B4664)
private val darkOnTertiaryContainer = Color(0xFFDAE2FF)
private val darkError = Color(0xFFFFB4AB)
private val darkErrorContainer = Color(0xFF93000A)
private val darkOnError = Color(0xFF690005)
private val darkOnErrorContainer = Color(0xFFFFDAD6)
private val darkBackground = Color(0xFF191C1D)
private val darkOnBackground = Color(0xFFE1E3E3)
private val darkSurface = Color(0xFF191C1D)
private val darkOnSurface = Color(0xFFE1E3E3)
private val darkSurfaceVariant = Color(0xFF3F484A)
private val darkOnSurfaceVariant = Color(0xFFBFC8CA)
private val darkOutline = Color(0xFF899294)
private val darkInverseOnSurface = Color(0xFF191C1D)
private val darkInverseSurface = Color(0xFFE1E3E3)
private val darkInversePrimary = Color(0xFF006874)
private val darkShadow = Color(0xFF000000)
private val darkSurfaceTint = Color(0xFF4FD8EB)

private val seed = Color(0xFF4FD8EB)

internal val LightColors = lightColorScheme(
    primary = lightPrimary,
    onPrimary = lightOnPrimary,
    primaryContainer = lightPrimaryContainer,
    onPrimaryContainer = lightOnPrimaryContainer,
    secondary = lightSecondary,
    onSecondary = lightOnSecondary,
    secondaryContainer = lightSecondaryContainer,
    onSecondaryContainer = lightOnSecondaryContainer,
    tertiary = lightTertiary,
    onTertiary = lightOnTertiary,
    tertiaryContainer = lightTertiaryContainer,
    onTertiaryContainer = lightOnTertiaryContainer,
    error = lightError,
    errorContainer = lightErrorContainer,
    onError = lightOnError,
    onErrorContainer = lightOnErrorContainer,
    background = lightBackground,
    onBackground = lightOnBackground,
    surface = lightSurface,
    onSurface = lightOnSurface,
    surfaceVariant = lightSurfaceVariant,
    onSurfaceVariant = lightOnSurfaceVariant,
    outline = lightOutline,
    inverseOnSurface = lightInverseOnSurface,
    inverseSurface = lightInverseSurface,
    inversePrimary = lightInversePrimary,
    surfaceTint = lightSurfaceTint,
)

internal val DarkColors = darkColorScheme(
    primary = darkPrimary,
    onPrimary = darkOnPrimary,
    primaryContainer = darkPrimaryContainer,
    onPrimaryContainer = darkOnPrimaryContainer,
    secondary = darkSecondary,
    onSecondary = darkOnSecondary,
    secondaryContainer = darkSecondaryContainer,
    onSecondaryContainer = darkOnSecondaryContainer,
    tertiary = darkTertiary,
    onTertiary = darkOnTertiary,
    tertiaryContainer = darkTertiaryContainer,
    onTertiaryContainer = darkOnTertiaryContainer,
    error = darkError,
    errorContainer = darkErrorContainer,
    onError = darkOnError,
    onErrorContainer = darkOnErrorContainer,
    background = darkBackground,
    onBackground = darkOnBackground,
    surface = darkSurface,
    onSurface = darkOnSurface,
    surfaceVariant = darkSurfaceVariant,
    onSurfaceVariant = darkOnSurfaceVariant,
    outline = darkOutline,
    inverseOnSurface = darkInverseOnSurface,
    inverseSurface = darkInverseSurface,
    inversePrimary = darkInversePrimary,
    surfaceTint = darkSurfaceTint,
)

private object Colors {
    val AppBG = Color(0xFF121212)
    val CardBackground = Color(0xFF272727)
    val AccentIcons = Color(0xFF6FEBFF)
    val AccentText = Color(0xFF8CE3E3)
}
