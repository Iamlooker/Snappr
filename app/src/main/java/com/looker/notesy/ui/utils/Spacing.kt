package com.looker.notesy.ui.utils

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
	val text: Dp = 8.dp,
	val border: Dp = 16.dp
)

val LocalSpacing = staticCompositionLocalOf<Spacing> { error("No Spacing Provided") }
