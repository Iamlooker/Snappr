package com.looker.notesy.feature_note.presentation.add_edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

	LaunchedEffect(true) {
		viewModel.eventFlow.collectLatest {
			when (it) {
				UiEvents.SaveNote -> {
					navController.navigateUp()
				}
				is UiEvents.ShowSnackBar -> {}
			}
		}
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.imePadding(),
		floatingActionButton = {
			FloatingActionButton(onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) }) {
				Icon(imageVector = Icons.Rounded.Done, contentDescription = null)
			}
		}
	) { paddingValues ->
		Column(
			modifier = Modifier.padding(paddingValues)
		) {
			TransparentTextField(
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