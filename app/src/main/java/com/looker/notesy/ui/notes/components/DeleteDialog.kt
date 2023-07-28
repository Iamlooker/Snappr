package com.looker.notesy.ui.notes.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.looker.notesy.domain.model.Note

@Composable
fun DeleteDialog(
	note: Note,
	onConfirm: () -> Unit = {},
	onDismiss: () -> Unit
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		confirmButton = {
			FilledTonalButton(
				onClick = onConfirm ,
				colors = ButtonDefaults.filledTonalButtonColors(
					containerColor = MaterialTheme.colorScheme.errorContainer,
					contentColor = MaterialTheme.colorScheme.onErrorContainer
				)
			) {
				Text(text = "Delete")
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text(text = "Cancel")
			}
		},
		icon = {
			Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
		},
		title = {
			Text(
				text = "Delete Note: ${note.title}",
				style = MaterialTheme.typography.titleLarge
			)
		},
		text = {
			Text(text = "Are you sure, you want to delete this note?")
		}
	)
}