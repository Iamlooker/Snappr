package com.looker.notesy.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@Composable
fun NotesyImage(
	data: Any?,
	modifier: Modifier = Modifier,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Fit,
	contentDescription: String? = null
) {
	AsyncImage(
		modifier = modifier,
		model = data,
		alignment = alignment,
		contentScale = contentScale,
		contentDescription = contentDescription
	)
}