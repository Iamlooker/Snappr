package com.looker.notesy.feature_note.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.core.UiEvents
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.use_case.NoteUseCases
import com.looker.notesy.feature_note.domain.utils.NoteOrder
import com.looker.notesy.feature_note.domain.utils.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class NotesViewModel
@Inject constructor(
	private val noteUseCases: NoteUseCases
) : ViewModel() {

	private val _state = MutableStateFlow(NotesState())
	val state = _state.asStateFlow()

	private val _eventFlow = MutableSharedFlow<UiEvents>()
	val eventFlow = _eventFlow.asSharedFlow()

	private var recentlyDeletedNote: Note? = null
	private val deletedNoteLock: Mutex = Mutex()
	private var getNotesJob: Job? = null

	init {
		getNotes(NoteOrder.Date(OrderType.Descending))
	}

	fun showDeleteDialog(note: Note? = null) {
		_state.update {
			it.copy(showDeleteDialog = note)
		}
	}

	fun deleteNote(note: Note) {
		viewModelScope.launch {
			noteUseCases.deleteNote(note)
			deletedNoteLock.withLock { recentlyDeletedNote = note }
			showDeleteDialog()
			_eventFlow.emit(UiEvents.ShowSnackBar("Restore Note?"))
		}
	}

	fun restoreNote() {
		viewModelScope.launch {
			noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
			deletedNoteLock.withLock { recentlyDeletedNote = null }
			_eventFlow.emit(UiEvents.Restored(recentlyDeletedNote))
		}
	}

	fun reorderNotes(order: NoteOrder) {
		if (state.value.noteOrder::class == order::class && state.value.noteOrder.orderType == order.orderType) return
		getNotes(order)
	}

	private fun getNotes(noteOrder: NoteOrder) {
		getNotesJob?.cancel()
		getNotesJob = noteUseCases.getNotes(noteOrder)
			.onEach { notes ->
				_state.emit(
					state.value.copy(
						notesList = notes,
						noteOrder = noteOrder
					)
				)
			}.launchIn(viewModelScope)
	}
}