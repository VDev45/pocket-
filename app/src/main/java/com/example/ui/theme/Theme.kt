package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PocketCoral,
    onPrimary = Color.White,
    primaryContainer = PocketCoralDark,
    onPrimaryContainer = Color.White,
    secondary = PocketTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF004D40),
    onSecondaryContainer = Color(0xFFE0F2F1),
    tertiary = PocketAmber,
    background = ReaderDarkBg,
    onBackground = ReaderDarkText,
    surface = ReaderDarkSurface,
    onSurface = ReaderDarkText,
    surfaceVariant = Color(0xFF272D34),
    onSurfaceVariant = ReaderDarkSecondary,
    outline = ReaderDarkDivider
)

private val LightColorScheme = lightColorScheme(
    primary = PocketCoral,
    onPrimary = Color.White,
    primaryContainer = PocketCoralContainer,
    onPrimaryContainer = PocketCoralDark,
    secondary = PocketTeal,
    onSecondary = Color.White,
    secondaryContainer = PocketTealContainer,
    onSecondaryContainer = Color(0xFF004D40),
    tertiary = PocketAmber,
    background = ReaderPaperBg,
    onBackground = ReaderPaperText,
    surface = Color.White,
    onSurface = ReaderPaperText,
    surfaceVariant = Color(0xFFF0EBE4),
    onSurfaceVariant = ReaderPaperSecondary,
    outline = ReaderPaperDivider
)

@Composable
fun PocketTheme(
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
