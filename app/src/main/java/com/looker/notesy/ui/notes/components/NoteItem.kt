package com.looker.notesy.ui.notes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.notesy.domain.model.Note
import com.looker.notesy.ui.components.FormattedNoteContent
import com.looker.notesy.ui.components.NoteId
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
	SwipeToDismiss(
		modifier = modifier
			.fillMaxWidth()
			.clip(MaterialTheme.shapes.large)
			.clickable(onClick = onNoteClick),
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
				border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
				shape = MaterialTheme.shapes.large
			) {
				Column(
					modifier = Modifier.padding(LocalSpacing.current.border),
					horizontalAlignment = Alignment.Start,
					verticalArrangement = Arrangement.Center
				) {
					NoteId(
						text = "# ${note.id}",
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.outline
					)
					Spacer(modifier = Modifier.height(LocalSpacing.current.text))
					if (note.title.isNotBlank()) {
						FormattedNoteContent(
							rawText = note.title.trim(),
							enableSelection = false,
							style = MaterialTheme.typography.titleMedium,
							fontWeight = FontWeight.SemiBold,
							maxLines = 1
						)
					}
					if (note.title.isNotBlank() && note.content.isNotBlank()) {
						Spacer(modifier = Modifier.height(LocalSpacing.current.text))
					}
					if (note.content.isNotBlank()) {
						FormattedNoteContent(
							rawText = note.content.trim(),
							enableSelection = false,
							style = MaterialTheme.typography.bodyMedium,
							maxLines = 8
						)
					}
				}
			}
		}
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