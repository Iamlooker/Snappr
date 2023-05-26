package com.looker.notesy.feature_note.presentation.add_edit_note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.notesy.feature_note.presentation.add_edit_note.components.TransparentTextField

@Composable
fun AddEditNoteScreen(
	viewModel: AddEditViewModel = hiltViewModel(),
	navigateUp: () -> Unit
) {
	BackHandler {
		viewModel.saveNote {}
		navigateUp()
	}

	val errorMessage = viewModel.errorMessage
	val snackbarHost = remember { SnackbarHostState() }
	val focusRequester = remember { FocusRequester() }

	LaunchedEffect(errorMessage) {
		if (errorMessage.isNotBlank()) {
			snackbarHost.showSnackbar(errorMessage)
		}
	}

	LaunchedEffect(true) {
		focusRequester.requestFocus()
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.imePadding(),
		floatingActionButton = {
			FloatingActionButton(onClick = { viewModel.saveNote(navigateUp) }) {
				Icon(imageVector = Icons.Rounded.Save, contentDescription = null)
			}
		},
		snackbarHost = { SnackbarHost(hostState = snackbarHost) }
	) { paddingValues ->
		Surface(tonalElevation = 8.dp) {
			Column(Modifier.padding(paddingValues)) {
				TransparentTextField(
					modifier = Modifier.fillMaxWidth(),
					text = viewModel.noteTitle,
					hint = "Title...",
					singleLine = true,
					containerColor = Color.Transparent,
					textStyle = MaterialTheme.typography.titleLarge,
					onValueChange = viewModel::updateTitle
				)
				TransparentTextField(
					modifier = Modifier
						.fillMaxSize()
						.focusRequester(focusRequester),
					text = viewModel.noteContent,
					hint = "Content...",
					onValueChange = viewModel::updateContent
				)
			}
		}
	}
}