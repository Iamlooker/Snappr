package com.looker.notesy.ui.add_edit_note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.notesy.R
import com.looker.notesy.ui.add_edit_note.components.TransparentTextField
import com.looker.notesy.ui.components.NavigationAppBar

@Composable
fun AddEditNoteScreen(
	viewModel: AddEditViewModel = hiltViewModel(),
	navigateUp: () -> Unit
) {
	BackHandler {
		viewModel.saveNote(navigateUp, showErrorSnackBar = false)
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
		snackbarHost = { SnackbarHost(hostState = snackbarHost) },
		contentWindowInsets = WindowInsets.statusBars
	) { paddingValues ->
		Surface(tonalElevation = 8.dp) {
			Column(Modifier.padding(paddingValues)) {
				NavigationAppBar(
					onBackPressed = { viewModel.saveNote(navigateUp, showErrorSnackBar = false) },
					title = {
						Text(
							text = stringResource(R.string.label_edit_mode),
							color = MaterialTheme.colorScheme.outline
						)
					}
				)
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