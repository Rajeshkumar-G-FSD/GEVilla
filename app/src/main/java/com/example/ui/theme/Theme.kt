package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = ForestGreenContainer,
    onPrimaryContainer = OnForestGreenContainer,
    secondary = ForestGreenLight,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD8E7DF),
    onSecondaryContainer = Color(0xFF10271E),
    tertiary = GoldAccent,
    onTertiary = Color.White,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = OnGoldContainer,
    background = WarmBeigeBackground,
    onBackground = TextPrimary,
    surface = WarmBeigeSurface,
    onSurface = TextPrimary,
    surfaceVariant = WarmBeigeSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = WarmBeigeBorder,
    outlineVariant = Color(0xFFE0DDD5),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF86D8AB),
    onPrimary = Color(0xFF003820),
    primaryContainer = ForestGreenDark,
    onPrimaryContainer = Color(0xFFA5F4C7),
    secondary = Color(0xFFB5CCBA),
    onSecondary = Color(0xFF203528),
    tertiary = GoldAccentLight,
    onTertiary = Color(0xFF422C00),
    tertiaryContainer = Color(0xFF5E4000),
    onTertiaryContainer = GoldContainer,
    background = Color(0xFF121614),
    onBackground = Color(0xFFE2E4E0),
    surface = Color(0xFF1A1F1C),
    onSurface = Color(0xFFE2E4E0),
    surfaceVariant = Color(0xFF262E29),
    onSurfaceVariant = Color(0xFFB8C2BC),
    outline = Color(0xFF434E47)
)

@Composable
fun GreenEdgeVillaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    GreenEdgeVillaTheme(darkTheme = darkTheme, content = content)
}
