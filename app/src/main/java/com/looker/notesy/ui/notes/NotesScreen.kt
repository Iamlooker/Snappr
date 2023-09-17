package com.looker.notesy.ui.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.notesy.R
import com.looker.notesy.ui.notes.components.DeleteDialog
import com.looker.notesy.ui.notes.components.NoteItem
import com.looker.notesy.ui.notes.components.OrderChips
import com.looker.notesy.ui.utils.LocalSpacing
import com.looker.notesy.ui.utils.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
	viewModel: NotesViewModel = hiltViewModel(),
	onCreateNewClick: () -> Unit,
	onNoteClick: (Int?) -> Unit
) {
	val state by viewModel.noteState.collectAsStateWithLifecycle()
	val deleteNoteConfirmation = state.showDeleteDialog
	val snackbarHost = remember { SnackbarHostState() }

	LaunchedEffect(state.snackBarMessage) {
		state.snackBarMessage?.let { snackbarHost.showSnackbar(it) }
		viewModel.snackBarConsumed()
	}

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		floatingActionButton = {
			ExtendedFloatingActionButton(
				containerColor = MaterialTheme.colorScheme.tertiaryContainer,
				onClick = onCreateNewClick
			) {
				Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
				Spacer(modifier = Modifier.width(LocalSpacing.current.text))
				Text(text = stringResource(R.string.action_add_note))
			}
		},
		floatingActionButtonPosition = FabPosition.Center,
		snackbarHost = {
			SnackbarHost(hostState = snackbarHost) {
				Snackbar(
					modifier = Modifier.padding(16.dp),
					action = {
						FilledTonalButton(onClick = viewModel::restoreNote) {
							Text(text = stringResource(R.string.action_restore))
						}
					}
				) {
					Text(text = stringResource(R.string.label_undo_delete))
				}
			}
		},
		contentWindowInsets = WindowInsets(0)
	) { paddingValue ->
		LazyVerticalStaggeredGrid(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 8.dp),
			contentPadding = paddingValue + PaddingValues(bottom = 88.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalItemSpacing = 8.dp,
			columns = StaggeredGridCells.Fixed(2)
		) {
			item(span = StaggeredGridItemSpan.FullLine) {
				OrderChips(noteOrder = state.noteOrder, onOrderChange = viewModel::reorderNotes)
			}
			items(
				items = state.notesList
			) { note ->
				val dismissState = rememberDismissState(
					confirmValueChange = {
						if (it != DismissValue.Default) viewModel.showDeleteDialog(note)
						true
					}
				)
				LaunchedEffect(deleteNoteConfirmation) {
					if (deleteNoteConfirmation != null) dismissState.reset()
				}
				NoteItem(
					note = note,
					state = dismissState,
					onNoteClick = { onNoteClick(note.id) }
				)
			}
		}
		if (deleteNoteConfirmation != null) {
			DeleteDialog(
				name = deleteNoteConfirmation.title,
				onConfirm = { viewModel.deleteNote(deleteNoteConfirmation) },
				onDismiss = viewModel::showDeleteDialog
			)
		}
	}
}