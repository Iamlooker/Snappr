package com.looker.notesy.feature_note.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
	note: Note,
	modifier: Modifier = Modifier,
	restore: Boolean = true,
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
	LaunchedEffect(restore) {
		if (restore) state.reset()
	}

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
						Text(
							text = note.title,
							style = MaterialTheme.typography.titleLarge,
							maxLines = 1
						)
					}
					if (note.title.isNotBlank()) {
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