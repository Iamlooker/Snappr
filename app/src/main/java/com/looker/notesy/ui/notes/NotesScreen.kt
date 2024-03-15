package com.looker.notesy.ui.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    viewModel: NotesViewModel,
    onCreateNewClick: () -> Unit,
    onNoteClick: (Int?) -> Unit,
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
            contentPadding = paddingValue + PaddingValues(bottom = 84.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            columns = StaggeredGridCells.Fixed(2)
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                OrderChips(noteOrder = state.noteOrder, onOrderChange = viewModel::reorderNotes)
            }
            items(
                items = state.notesList,
                key = { it.id!! },
                contentType = { it.contentType }
            ) { note ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it != SwipeToDismissBoxValue.Settled) viewModel.showDeleteDialog(note)
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