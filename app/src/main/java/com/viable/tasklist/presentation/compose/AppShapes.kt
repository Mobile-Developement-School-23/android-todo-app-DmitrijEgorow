package com.viable.tasklist.presentation.compose

import androidx.compose.material.Shapes
import androidx.compose.runtime.staticCompositionLocalOf

private val Shapes = Shapes(
)

internal val LocalAppShapes = staticCompositionLocalOf { Shapes }