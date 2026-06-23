package com.szmaou.miramal.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue700,
    onPrimary = White,
    primaryContainer = Blue200,
    secondary = Blue500,
    background = White,
    surface = LightGray,
    onBackground = DarkGray,
    onSurface = DarkGray
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue200,
    onPrimary = DarkGray,
    primaryContainer = Blue700,
    secondary = Blue500,
    background = DarkGray,
    surface = DarkGray,
    onBackground = White,
    onSurface = White
)

@Composable
fun MiraMALTheme(
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
