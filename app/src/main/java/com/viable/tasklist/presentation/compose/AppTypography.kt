package com.viable.tasklist.presentation.compose

import androidx.compose.material.Typography
import androidx.compose.runtime.staticCompositionLocalOf

private val Typography = Typography()

internal val LocalAppTypography = staticCompositionLocalOf { Typography }
