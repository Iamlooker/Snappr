package com.looker.notesy.ui.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.notesy.domain.model.Note
import com.looker.notesy.ui.utils.noteFormatter
import com.looker.notesy.ui.theme.NotesyTheme
import com.looker.notesy.ui.utils.LocalSpacing

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
				val alignment by remember(direction) {
					derivedStateOf {
						when (direction) {
							DismissDirection.StartToEnd -> Alignment.CenterStart
							DismissDirection.EndToStart -> Alignment.CenterEnd
						}
					}
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
			},
			dismissContent = {
				Surface(
					modifier = Modifier.fillMaxWidth(),
					color = MaterialTheme.colorScheme.surfaceVariant,
					tonalElevation = 8.dp,
					shape = MaterialTheme.shapes.extraLarge
				) {
					Column(
						modifier = Modifier.padding(LocalSpacing.current.border),
						horizontalAlignment = Alignment.Start,
						verticalArrangement = Arrangement.Center
					) {
						if (note.title.isNotBlank()) {
							NoteText(
								message = note.title,
								style = MaterialTheme.typography.titleLarge
							)
						}
						if (note.title.isNotBlank() && note.content.isNotBlank()) {
							Spacer(modifier = Modifier.height(LocalSpacing.current.text))
						}
						if (note.content.isNotBlank()) {
							NoteText(
								message = note.content,
								style = MaterialTheme.typography.bodyMedium,
								maxLines = 8
							)
						}
					}
				}
			}
		)
	}
}

@Composable
fun NoteText(
	message: String,
	style: TextStyle,
	maxLines: Int = 1,
	overflow: TextOverflow = TextOverflow.Ellipsis
) {
	val styleText = noteFormatter(text = message)

	Text(
		text = styleText,
		maxLines = maxLines,
		overflow = overflow,
		style = style.copy(color = LocalContentColor.current)
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NotePrev() {
	NotesyTheme {
		NoteItem(
			note = Note(
				"Title",
				content = "This is test note, please check it out. ".repeat(20),
				timeCreated = 0L,
				color = MaterialTheme.colorScheme.tertiary.toArgb()
			)
		)
	}
}