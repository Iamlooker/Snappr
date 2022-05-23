package com.looker.notesy.feature_note.presentation.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.looker.notesy.R
import com.looker.notesy.feature_note.presentation.notes.components.NoteItem
import com.looker.notesy.feature_note.presentation.notes.components.OrderChips

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
	navController: NavController,
	viewModel: NotesViewModel = viewModel()
) {
	val state by viewModel.state.collectAsState()
	var showDialog by remember { mutableStateOf(false) }
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			SmallTopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
		},
		floatingActionButton = {
			FloatingActionButton(onClick = { }) {
				Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
			}
		}
	) {
		LazyColumn(
			contentPadding = it,
			verticalArrangement = Arrangement.spacedBy(16.dp)
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
						.animateItemPlacement()
				) {
					viewModel.onEvent(NotesEvent.Delete(note))
				}
			}
		}
	}
}