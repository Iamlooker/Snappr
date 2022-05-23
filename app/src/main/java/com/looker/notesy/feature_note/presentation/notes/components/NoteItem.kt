package com.looker.notesy.feature_note.presentation.notes.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.notesy.core.DismissDirection
import com.looker.notesy.core.DismissValue
import com.looker.notesy.core.SwipeToDismiss
import com.looker.notesy.core.rememberDismissState
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.ui.theme.NotesyTheme
import com.looker.notesy.ui.theme.RedPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
	note: Note,
	modifier: Modifier = Modifier,
	onNoteClick: () -> Unit = {},
	onDismiss: () -> Unit = {}
) {
	val state = rememberDismissState(
		confirmStateChange = {
			if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd) {
				onDismiss()
			}
			true
		}
	)

	Surface(
		modifier = modifier.fillMaxWidth(),
		color = MaterialTheme.colorScheme.surfaceVariant,
		tonalElevation = 8.dp,
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
						.background(MaterialTheme.colorScheme.errorContainer),
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
					Text(
						text = note.title,
						style = MaterialTheme.typography.titleLarge,
						maxLines = 1
					)
					Text(
						text = note.content,
						style = MaterialTheme.typography.bodyMedium,
						maxLines = 8,
						overflow = TextOverflow.Ellipsis
					)
				}
			}
		}
	}
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
				color = RedPink.toArgb()
			)
		)
	}
}