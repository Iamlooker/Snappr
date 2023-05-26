package com.looker.notesy.feature_note.presentation.add_edit_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.looker.notesy.core.UiEvents
import com.looker.notesy.feature_note.presentation.add_edit_note.components.TransparentTextField
import com.looker.notesy.feature_note.presentation.utils.extension.surfaceColorAtElevation

@Composable
fun AddEditNoteScreen(
	viewModel: AddEditViewModel = hiltViewModel(),
	navigateUp: () -> Unit
) {
	val title by viewModel.noteTitle.collectAsState()
	val content by viewModel.noteContent.collectAsState()

	val snackBarState by viewModel.eventFlow.collectAsState(UiEvents.EMPTY)
	val snackbarHost = remember { SnackbarHostState() }
	LaunchedEffect(snackBarState) {
		when (snackBarState) {
			UiEvents.SaveNote -> navigateUp()
			is UiEvents.ShowSnackBar -> snackbarHost.showSnackbar(
				message = (snackBarState as UiEvents.ShowSnackBar).message
			)
			else -> {}
		}
	}

	val focusRequester = remember { FocusRequester() }

	LaunchedEffect(true) {
		focusRequester.requestFocus()
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.imePadding(),
		floatingActionButton = {
			FloatingActionButton(onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) }) {
				Icon(imageVector = Icons.Rounded.Done, contentDescription = null)
			}
		},
		snackbarHost = {
			SnackbarHost(hostState = snackbarHost)
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
				.padding(paddingValues)
		) {
			TransparentTextField(
				modifier = Modifier.fillMaxWidth(),
				text = title.text,
				hint = title.hint,
				singleLine = true,
				containerColor = Color.Transparent,
				textStyle = MaterialTheme.typography.titleLarge,
				onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) }
			)
			TransparentTextField(
				modifier = Modifier
					.fillMaxSize()
					.focusRequester(focusRequester),
				text = content.text,
				hint = content.hint,
				onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) }
			)
		}
	}
}