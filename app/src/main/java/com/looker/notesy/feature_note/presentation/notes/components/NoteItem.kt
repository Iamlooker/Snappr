package com.looker.notesy.feature_note.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.notesy.core.DismissDirection
import com.looker.notesy.core.DismissState
import com.looker.notesy.core.SwipeToDismiss
import com.looker.notesy.core.rememberDismissState
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.presentation.utils.SymbolAnnotationType
import com.looker.notesy.feature_note.presentation.utils.noteFormatter
import com.looker.notesy.ui.theme.NotesyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
	note: Note,
	modifier: Modifier = Modifier,
	state: DismissState = rememberDismissState(),
	onNoteClick: () -> Unit = {}
) {
	ElevatedCard(
		modifier = modifier.fillMaxWidth(),
		shape = MaterialTheme.shapes.extraLarge,
		onClick = onNoteClick
	) {
		SwipeToDismiss(
			state = state,
			background = {
				val direction = state.dismissDirection ?: return@SwipeToDismiss
				val alignment = when (direction) {
					DismissDirection.StartToEnd -> Alignment.CenterStart
					DismissDirection.EndToStart -> Alignment.CenterEnd
				}
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(MaterialTheme.colorScheme.errorContainer)
						.absolutePadding(left = 10.dp, right = 10.dp),
					contentAlignment = alignment
				) {
					Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
				}
			}
		) {
			Surface(
				modifier = Modifier.fillMaxWidth(),
				color = MaterialTheme.colorScheme.surfaceVariant,
				tonalElevation = 8.dp,
				shape = MaterialTheme.shapes.extraLarge
			) {
				Column(
					modifier = Modifier.padding(16.dp),
					horizontalAlignment = Alignment.Start,
					verticalArrangement = Arrangement.Center
				) {
					if (note.title.isNotBlank()) {
						NoteText(
							message = note.title,
							style = MaterialTheme.typography.titleLarge,
							noteClicked = { onNoteClick() }
						)
					}
					if (note.content.isNotBlank()) {
						NoteText(
							message = note.content,
							style = MaterialTheme.typography.bodyMedium,
							maxLines = 8,
							noteClicked = { onNoteClick() }
						)
					}
				}
			}
		}
	}
}

@Composable
fun NoteText(
	message: String,
	style: TextStyle,
	maxLines: Int = 1,
	overflow: TextOverflow = TextOverflow.Clip,
	noteClicked: (String) -> Unit
) {
	val uriHandler = LocalUriHandler.current

	val styleText = noteFormatter(text = message)

	ClickableText(
		text = styleText,
		maxLines = maxLines,
		overflow = overflow,
		style = style.copy(color = LocalContentColor.current),
		onClick = {
			styleText
				.getStringAnnotations(start = it, end = it)
				.firstOrNull()
				?.let { annotation ->
					when (annotation.tag) {
						SymbolAnnotationType.NOTE.name -> noteClicked(annotation.item)
						SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
					}
				}
		}
	)
}

@Preview
@Composable
fun NotePrev() {
	NotesyTheme {
		NoteItem(
			note = Note(
				"Title",
				content = "This is test note, please check it out. ".repeat(10),
				timeCreated = 0L,
				color = MaterialTheme.colorScheme.tertiary.toArgb()
			)
		)
	}
}