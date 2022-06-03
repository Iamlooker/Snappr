package com.looker.notesy.feature_note.presentation.add_edit_note

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.looker.notesy.core.UiEvents
import com.looker.notesy.feature_note.presentation.add_edit_note.components.TransparentTextField
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
	navController: NavController,
	viewModel: AddEditViewModel = hiltViewModel()
) {
	val title by viewModel.noteTitle.collectAsState()
	val content by viewModel.noteContent.collectAsState()

	var snackBarState: UiEvents.ShowSnackBar by remember { mutableStateOf(UiEvents.ShowSnackBar()) }
	LaunchedEffect(true) {
		viewModel.eventFlow.collectLatest {
			when (it) {
				is UiEvents.ShowSnackBar -> {
					snackBarState = it
				}
				UiEvents.SaveNote -> {
					navController.navigateUp()
				}
			}
		}
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.statusBarsPadding()
			.navigationBarsPadding()
			.imePadding(),
		floatingActionButton = {
			FloatingActionButton(onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) }) {
				Icon(imageVector = Icons.Rounded.Done, contentDescription = null)
			}
		},
		snackbarHost = {
			if (snackBarState.show) {
				Snackbar(
					containerColor = MaterialTheme.colorScheme.inverseSurface,
					contentColor = MaterialTheme.colorScheme.inverseOnSurface
				) {
					Text(text = snackBarState.message)
				}
			}
		}
	) { paddingValues ->
		Column(
			modifier = Modifier.padding(
				top = paddingValues.calculateTopPadding() + 32.dp,
				bottom = paddingValues.calculateBottomPadding()
			)
		) {
			TransparentTextField(
				modifier = Modifier.fillMaxWidth(),
				text = title.text,
				hint = title.hint,
				singleLine = true,
				textStyle = MaterialTheme.typography.titleLarge,
				onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) }
			)
			TransparentTextField(
				modifier = Modifier.fillMaxSize(),
				text = content.text,
				hint = content.hint,
				onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) }
			)
		}
	}
}