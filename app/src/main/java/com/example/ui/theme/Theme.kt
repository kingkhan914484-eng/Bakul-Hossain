package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmergencyRed,
    onPrimary = Color.White,
    primaryContainer = EmergencyRedContainer,
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = AccentCyan,
    onSecondary = Color(0xFF00364F),
    secondaryContainer = Color(0xFF004D6E),
    onSecondaryContainer = Color(0xFFC7E7FF),
    tertiary = VerifiedGreen,
    onTertiary = Color.White,
    tertiaryContainer = VerifiedGreenContainer,
    onTertiaryContainer = VerifiedGreenText,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceCardBorder,
    error = Color(0xFFFF5449),
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // The emergency directory mandates a crisp, premium, high-contrast dark theme
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
