package com.looker.notesy.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.ui.theme.NotesyTheme
import com.looker.notesy.ui.theme.RedPink

@Composable
fun NoteItem(
	note: Note,
	modifier: Modifier = Modifier
) {
	Surface(
		modifier = modifier.fillMaxWidth(),
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