package com.looker.notesy.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val shapes: Shapes = Shapes(
	extraSmall = RoundedCornerShape(8.dp),
	small = RoundedCornerShape(12.dp),
	medium = RoundedCornerShape(16.dp),
	large = RoundedCornerShape(18.dp),
	extraLarge = RoundedCornerShape(24.dp)
)

fun CornerBasedShape.top(): CornerBasedShape = copy(
	bottomEnd = CornerSize(0.dp),
	bottomStart = CornerSize(0.dp)
)

fun CornerBasedShape.bottom(): CornerBasedShape = copy(
	topEnd = CornerSize(0.dp),
	topStart = CornerSize(0.dp)
)