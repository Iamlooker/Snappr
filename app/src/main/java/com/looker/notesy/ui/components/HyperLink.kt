package com.looker.notesy.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.math.sin

private const val WAVE_AMPLITUDE = 2f
private const val WAVE_WAVELENGTH = 4f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HyperLink(
    link: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    color: Color = MaterialTheme.colorScheme.primary,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    lineThickness: Dp = 1.2.dp,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 12.dp)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = null,
                tint = color
            )
            Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                Text(
                    text = link,
                    style = style,
                    color = color,
                    maxLines = maxLines,
                    overflow = overflow
                )
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    val offsets = (0..size.width.roundToInt()).map { xAxis ->
                        Offset(
                            x = xAxis.toFloat(),
                            y = WAVE_AMPLITUDE * sin(xAxis.toFloat() / WAVE_WAVELENGTH)
                        )
                    }
                    drawPoints(
                        points = offsets,
                        pointMode = PointMode.Points,
                        color = color,
                        strokeWidth = lineThickness.toPx(),
                        cap = StrokeCap.Round,
                        alpha = 0.3f
                    )
                }
            }
        }
    }
}