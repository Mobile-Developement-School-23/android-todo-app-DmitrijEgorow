package com.viable.tasklist.presentation.compose

import androidx.compose.material.*
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val mainColor = Color(0xFFBE62FF)

data class AppColors(
    val themeColors: Colors,

    // Custom colors here
    val topAppBarBackground: Color = mainColor,
)

internal val LightColorPalette = AppColors(
    themeColors = lightColors(),
)

internal val DarkColorPalette = AppColors(
    themeColors = darkColors(),
)

internal val LocalAppColors = staticCompositionLocalOf { LightColorPalette }
