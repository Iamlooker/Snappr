package com.looker.notesy.feature_note.presentation.add_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.core.UiEvents
import com.looker.notesy.feature_note.domain.model.InvalidNoteException
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel
@Inject constructor(
	private val noteUseCases: NoteUseCases,
	savedStateHandle: SavedStateHandle
) : ViewModel() {
	private val _noteTitle = MutableStateFlow(NoteTextFieldState(hint = "Title..."))
	val noteTitle = _noteTitle.asStateFlow()

	private val _noteContent = MutableStateFlow(NoteTextFieldState(hint = "Note..."))
	val noteContent = _noteContent.asStateFlow()

	private val _eventFlow = MutableSharedFlow<UiEvents>()
	val eventFlow = _eventFlow.asSharedFlow()

	private var currentNoteId: Int? = null

	init {
		savedStateHandle.get<Int>("noteId")?.let { noteId ->
			if (noteId != -1) {
				viewModelScope.launch {
					noteUseCases.getNote(noteId)?.let { note ->
						currentNoteId = note.id
						_noteTitle.value = noteTitle.value.copy(text = note.title)
						_noteContent.value = noteTitle.value.copy(text = note.content)
					}
				}
			}
		}
	}

	fun onEvent(event: AddEditNoteEvent) {
		when (event) {
			is AddEditNoteEvent.EnteredTitle -> {
				_noteTitle.value = noteTitle.value.copy(text = event.value)
			}
			is AddEditNoteEvent.EnteredContent -> {
				_noteContent.value = noteContent.value.copy(text = event.value)
			}
			AddEditNoteEvent.SaveNote -> {
				viewModelScope.launch {
					try {
						noteUseCases.addNote(
							Note(
								title = noteTitle.value.text,
								content = noteContent.value.text,
								timeCreated = System.currentTimeMillis(),
								color = 0,
								id = currentNoteId
							)
						)
						_eventFlow.emit(UiEvents.SaveNote)
					} catch (e: InvalidNoteException) {
						_eventFlow.emit(UiEvents.ShowSnackBar(e.message ?: "Couldn't Save Note!!"))
					}
				}
			}
		}
	}
}