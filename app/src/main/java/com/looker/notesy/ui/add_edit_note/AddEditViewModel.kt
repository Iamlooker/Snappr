package com.looker.notesy.ui.add_edit_note

import androidx.compose.runtime.*
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.domain.model.InvalidNoteException
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository
import com.looker.notesy.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel
@Inject constructor(
	private val noteUseCases: NoteUseCases,
	private val repository: NoteRepository,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val inputNoteId = savedStateHandle.get<Int?>("noteId")?.takeIf { it != -1 }

	val isIdEditable = inputNoteId == null

	var noteId by mutableStateOf("")
		private set

	var isIdValid = snapshotFlow { noteId }
		.map {
			val id = it.removePrefix("# ")
			if (id.isDigitsOnly() && id.toIntOrNull() != null) {
				noteUseCases.getNote(id.toInt()).first() == null
			} else false
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = true
		)

	var noteTitle by mutableStateOf("")
		private set

	var noteContent by mutableStateOf("")
		private set

	var errorMessage by mutableStateOf("")
		private set

	init {
		viewModelScope.launch {
			if (inputNoteId != null) {
				noteUseCases
					.getNote(inputNoteId)
					.filterNotNull()
					.collectLatest { note ->
						noteId = "# ${note.id}"
						noteTitle = note.title
						noteContent = note.content
					}
			} else {
				launch {
					repository
						.getLastNoteId()
						.collectLatest {
							noteId = "# " + (if (it == null) 1 else (it + 1)).toString()
						}
				}
			}
			launch {
				snapshotFlow { noteId }
					.collectLatest { }
			}
		}
	}

	fun updateNoteId(id: String) {
		noteId = if (!id.startsWith("# ") && id.toIntOrNull() != null) "# $id"
		else id
	}

	fun updateTitle(title: String) {
		noteTitle = title
	}

	fun updateContent(content: String) {
		noteContent = content
	}

	fun saveNote(onSuccess: () -> Unit, showErrorSnackBar: Boolean = true) {
		viewModelScope.launch {
			try {
				noteUseCases.addNote(
					Note(
						title = noteTitle,
						content = noteContent,
						timeCreated = System.currentTimeMillis(),
						color = 0,
						id = inputNoteId
					)
				)
			} catch (e: InvalidNoteException) {
				if (showErrorSnackBar) errorMessage = e.message ?: "Note not saved"
			} finally {
				onSuccess()
			}
		}
	}
}