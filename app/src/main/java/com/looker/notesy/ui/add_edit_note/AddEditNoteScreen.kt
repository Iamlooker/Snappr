package com.looker.notesy.ui.add_edit_note

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.notesy.R
import com.looker.notesy.ui.add_edit_note.components.TransparentTextField
import com.looker.notesy.ui.components.NavigationAppBar
import com.looker.notesy.ui.components.NoteId
import com.looker.notesy.ui.utils.LocalSpacing

@Composable
fun AddEditNoteScreen(
	viewModel: AddEditViewModel = hiltViewModel(),
	navigateUp: () -> Unit
) {
	val errorMessage = viewModel.errorMessage
	val context = LocalContext.current
	val snackbarHost = remember { SnackbarHostState() }
	val focusRequester = remember { FocusRequester() }

	LaunchedEffect(errorMessage) {
		if (errorMessage != -1) {
			snackbarHost.showSnackbar(context.getString(errorMessage))
		}
	}

	LaunchedEffect(true) {
		focusRequester.requestFocus()
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.imePadding(),
		snackbarHost = { SnackbarHost(hostState = snackbarHost) },
		floatingActionButton = {
			FloatingActionButton(
				modifier = Modifier.padding(bottom = 8.dp),
				onClick = { viewModel.saveNote(navigateUp) }
			) {
				Icon(imageVector = Icons.Rounded.Save, contentDescription = null)
			}
		},
		contentWindowInsets = WindowInsets.statusBars
	) { paddingValues ->
		Surface(tonalElevation = 8.dp) {
			Column(Modifier.padding(paddingValues)) {
				NavigationAppBar(
					onBackPressed = { viewModel.saveNote(navigateUp) },
					title = {
						Text(
							text = stringResource(R.string.label_edit_mode),
							color = MaterialTheme.colorScheme.outline
						)
					}
				)
				if (viewModel.isIdEditable) {
					val isNewNoteIdValid by viewModel.isIdValid.collectAsStateWithLifecycle()

					TransparentTextField(
						text = viewModel.noteId,
						hint = "ID",
						onValueChange = viewModel::updateNoteId,
						singleLine = true,
						containerColor = Color.Transparent,
						textStyle = LocalTextStyle.current,
						isError = !isNewNoteIdValid
					)
				} else {
					NoteId(
						modifier = Modifier.padding(LocalSpacing.current.border),
						text = viewModel.noteId,
						color = MaterialTheme.colorScheme.outline
					)
				}
				TransparentTextField(
					modifier = Modifier.fillMaxWidth(),
					text = viewModel.noteTitle,
					hint = stringResource(R.string.hint_title),
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
					hint = stringResource(R.string.hint_content),
					onValueChange = viewModel::updateContent
				)
			}
		}
	}
}