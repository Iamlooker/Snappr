package com.looker.notesy.ui.notes.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.looker.notesy.R
import com.looker.notesy.domain.model.Note

@Composable
fun DeleteDialog(
	name: String,
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
				Text(text = stringResource(R.string.action_delete))
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text(text = stringResource(R.string.action_cancel))
			}
		},
		icon = {
			Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
		},
		title = {
			Text(
				text = stringResource(R.string.label_delete_heading, name),
				style = MaterialTheme.typography.titleLarge
			)
		},
		text = {
			Text(text = stringResource(R.string.label_delete_confirmation))
		}
	)
}