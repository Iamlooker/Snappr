package com.looker.notesy.ui.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.notesy.R
import com.looker.notesy.ui.notes.components.DeleteDialog
import com.looker.notesy.ui.notes.components.NoteItem
import com.looker.notesy.ui.notes.components.OrderChips
import com.looker.notesy.ui.utils.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
	viewModel: NotesViewModel = hiltViewModel(),
	onCreateNewClick: () -> Unit,
	onNoteClick: (Int?) -> Unit
) {
	val state by viewModel.noteState.collectAsStateWithLifecycle()
	val deleteNoteConfirmation = state.showDeleteDialog
	val topAppBarScrollBehavior = rememberTopAppBarState()
	val scrollBehavior = pinnedScrollBehavior(topAppBarScrollBehavior)
	val snackbarHost = remember { SnackbarHostState() }

	LaunchedEffect(state.snackBarMessage) {
		state.snackBarMessage?.let { snackbarHost.showSnackbar(it) }
		viewModel.snackBarConsumed()
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(
						text = stringResource(id = R.string.app_name),
						style = MaterialTheme.typography.headlineMedium
					)
				},
				scrollBehavior = scrollBehavior
			)
		},
		floatingActionButton = {
			ExtendedFloatingActionButton(
				containerColor = MaterialTheme.colorScheme.tertiaryContainer,
				onClick = onCreateNewClick
			) {
				Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
				Spacer(modifier = Modifier.width(LocalSpacing.current.text))
				Text(text = "Add Note")
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
		}
	) { paddingValue ->
		LazyColumn(
			contentPadding = paddingValue,
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			item {
				OrderChips(noteOrder = state.noteOrder, onOrderChange = viewModel::reorderNotes)
			}
			items(
				items = state.notesList,
				key = { note -> note.id ?: -1 }
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
					modifier = Modifier
						.padding(horizontal = LocalSpacing.current.border)
						.animateItemPlacement(),
					onNoteClick = { onNoteClick(note.id) }
				)
			}
		}
		if (deleteNoteConfirmation != null) {
			DeleteDialog(
				note = deleteNoteConfirmation,
				onConfirm = { viewModel.deleteNote(deleteNoteConfirmation) },
				onDismiss = viewModel::showDeleteDialog
			)
		}
	}
}