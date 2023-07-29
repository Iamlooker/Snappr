package com.looker.notesy.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NoteId(
	text: String,
	modifier: Modifier = Modifier,
	style: TextStyle = LocalTextStyle.current,
	color: Color = LocalContentColor.current,
	maxLines: Int = 1,
	overflow: TextOverflow = TextOverflow.Ellipsis,
	dotSpacing: Dp = 6.dp,
	lineThickness: Dp = 2.dp
) {
	Column(modifier = modifier.width(IntrinsicSize.Max)) {
		Text(
			text = text,
			style = style,
			color = color,
			maxLines = maxLines,
			overflow = overflow
		)
		Canvas(modifier = Modifier.fillMaxWidth()) {
			drawLine(
				color = color,
				start = Offset.Zero,
				end = Offset(size.width, 0f),
				cap = StrokeCap.Round,
				strokeWidth = lineThickness.toPx(),
				pathEffect = PathEffect.dashPathEffect(
					floatArrayOf(
						10f,
						dotSpacing.toPx()
					)
				)
			)
		}
	}
}