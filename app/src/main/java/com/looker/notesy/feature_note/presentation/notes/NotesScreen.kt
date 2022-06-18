package com.looker.notesy.feature_note.presentation.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.looker.notesy.R
import com.looker.notesy.core.UiEvents
import com.looker.notesy.feature_note.presentation.notes.components.NoteItem
import com.looker.notesy.feature_note.presentation.notes.components.OrderChips
import com.looker.notesy.feature_note.presentation.utils.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
	navController: NavController,
	viewModel: NotesViewModel = hiltViewModel()
) {
	val state by viewModel.state.collectAsState()
	val topAppBarScrollBehavior = rememberTopAppBarScrollState()
	val scrollBehavior = remember { pinnedScrollBehavior(topAppBarScrollBehavior) }
	val snackBarState by viewModel.eventFlow.collectAsState(UiEvents.ShowSnackBar())
	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			CenterAlignedTopAppBar(
				modifier = Modifier.windowInsetsTopHeight(
					WindowInsets.statusBars.add(WindowInsets(top = 64.dp))
				),
				title = {
					Text(
						modifier = Modifier.statusBarsPadding(),
						text = stringResource(id = R.string.app_name),
						style = MaterialTheme.typography.headlineMedium
					)
				},
				scrollBehavior = scrollBehavior
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				modifier = Modifier.navigationBarsPadding(),
				onClick = { navController.navigate(Screen.AddEditNoteScreen.route) }
			) {
				Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
			}
		},
		snackbarHost = {
			if ((snackBarState as UiEvents.ShowSnackBar).show) {
				Snackbar(
					containerColor = MaterialTheme.colorScheme.inverseSurface,
					contentColor = MaterialTheme.colorScheme.inverseOnSurface,
					action = {
						TextButton(onClick = { viewModel.onEvent(NotesEvent.Restore) }) {
							Text(text = "Restore", color = MaterialTheme.colorScheme.inversePrimary)
						}
					}
				) {
					Text(text = (snackBarState as UiEvents.ShowSnackBar).message)
				}
			}
		}
	) {
		LazyColumn(
			contentPadding = PaddingValues(
				top = it.calculateTopPadding(),
				bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
						+ 12.dp
			),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			item {
				OrderChips(noteOrder = state.noteOrder) { notesOrder ->
					viewModel.onEvent(NotesEvent.Order(notesOrder))
				}
			}
			items(items = state.notesList, key = { note -> note.id ?: 0 }) { note ->
				NoteItem(
					note = note,
					modifier = Modifier
						.padding(horizontal = 16.dp)
						.animateItemPlacement(),
					onNoteClick = {
						navController.navigate(
							route = Screen.AddEditNoteScreen.route + "?noteId=${note.id}"
						)
					}
				) {
					viewModel.onEvent(NotesEvent.Delete(note))
				}
			}
		}
	}
}