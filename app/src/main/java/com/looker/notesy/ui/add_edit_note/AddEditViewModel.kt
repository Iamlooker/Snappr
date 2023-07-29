package com.looker.notesy.ui.add_edit_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.domain.model.InvalidNoteException
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel
@Inject constructor(
	private val noteUseCases: NoteUseCases,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val noteId = savedStateHandle.get<Int?>("noteId")?.takeIf { it != -1 }

	var noteTitle by mutableStateOf("")
		private set

	var noteContent by mutableStateOf("")
		private set

	var errorMessage by mutableStateOf("")
		private set

	init {
		if (noteId != null) {
			viewModelScope.launch {
				noteUseCases
					.getNote(noteId)
					.filterNotNull()
					.collectLatest { note ->
						noteTitle = note.title
						noteContent = note.content
					}
			}
		}
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
						id = noteId
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