package com.viable.tasklist.presentation.compose

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

class AppDimensions {
    val spacingNormal = 16.dp
}

internal val LocalAppDimensions = staticCompositionLocalOf { AppDimensions() }
