package com.looker.notesy.feature_note.presentation.add_edit_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.looker.notesy.core.UiEvents
import com.looker.notesy.feature_note.presentation.add_edit_note.components.TransparentTextField
import com.looker.notesy.feature_note.presentation.utils.extension.surfaceColorAtElevation
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
				is UiEvents.ShowSnackBar -> snackBarState = it
				UiEvents.SaveNote -> navController.navigateUp()
			}
		}
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
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
			modifier = Modifier
				.background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
				.padding(
					top = paddingValues.calculateTopPadding() + 64.dp,
					bottom = paddingValues.calculateBottomPadding()
				)
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
				modifier = Modifier.fillMaxSize(),
				text = content.text,
				hint = content.hint,
				onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) }
			)
		}
	}
}