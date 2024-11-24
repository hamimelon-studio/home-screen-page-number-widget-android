package com.mike.widgetind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberpunkDarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    secondary = NeonPink,
    tertiary = NeonGreen,
    background = DarkBackground,
    surface = DialogSurface,          // Changed to darker surface
    surfaceVariant = DialogBackground, // Added for dialog background
    onSurfaceVariant = Color.White,   // For content on dialog background
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = NeonBlue,
    onSurface = Color.White,
    surfaceTint = Color.White,        // Added for better dialog appearance
    scrim = Color(0xFF0A0A0F)         // Added for dialog overlay
)

private val CyberpunkLightColorScheme = lightColorScheme(
    // Keep light theme the same as before
    primary = NeonBlue,
    secondary = NeonPurple,
    tertiary = NeonPink,
    background = Color(0xFFF2F2F7),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = CyberGray,
    onBackground = CyberGray,
    onSurface = CyberGray
)

@Composable
fun WidgetIndTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CyberpunkDarkColorScheme else CyberpunkLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Customize typography for modern fonts
        content = content
    )
}