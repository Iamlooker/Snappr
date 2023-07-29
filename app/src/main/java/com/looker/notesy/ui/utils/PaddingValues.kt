package com.looker.notesy.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection

@Composable
operator fun PaddingValues.plus(paddingValues: PaddingValues): PaddingValues = PaddingValues(
	start = calculateStartPadding(LocalLayoutDirection.current) + paddingValues.calculateStartPadding(LocalLayoutDirection.current),
	end = calculateEndPadding(LocalLayoutDirection.current) + paddingValues.calculateEndPadding(LocalLayoutDirection.current),
	top = calculateTopPadding() + paddingValues.calculateTopPadding(),
	bottom = calculateBottomPadding() + paddingValues.calculateBottomPadding(),
)