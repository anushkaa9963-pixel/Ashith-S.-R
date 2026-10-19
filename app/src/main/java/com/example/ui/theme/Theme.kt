package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PostureColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF041E26),
    primaryContainer = Color(0xFF004D5A),
    onPrimaryContainer = Color(0xFF99F5FF),
    secondary = NeonLime,
    onSecondary = Color(0xFF00220D),
    secondaryContainer = Color(0xFF005322),
    onSecondaryContainer = Color(0xFF88FFAA),
    tertiary = NeonAmber,
    onTertiary = Color(0xFF261D00),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PostureColorScheme,
        typography = Typography,
        content = content
    )
}
