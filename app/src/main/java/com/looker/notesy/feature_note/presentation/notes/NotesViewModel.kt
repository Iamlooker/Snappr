package com.looker.notesy.feature_note.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.core.UiEvents
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.use_case.NoteUseCases
import com.looker.notesy.feature_note.domain.utils.NoteOrder
import com.looker.notesy.feature_note.domain.utils.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
	private var getNotesJob: Job? = null

	init {
		getNotes(NoteOrder.Date(OrderType.Descending))
	}

	fun onEvent(event: NotesEvent) {
		when (event) {
			is NotesEvent.Delete -> {
				viewModelScope.launch {
					noteUseCases.deleteNote(event.note)
					recentlyDeletedNote = event.note
					_eventFlow.emit(UiEvents.ShowSnackBar("Restore Note?", true))
					launch {
						delay(2000)
						_eventFlow.emit(UiEvents.ShowSnackBar(show = false))
					}
				}
			}
			is NotesEvent.Order -> {
				if (state.value.noteOrder::class == event.noteOrder::class && state.value.noteOrder.orderType == event.noteOrder.orderType) return
				getNotes(event.noteOrder)
			}
			NotesEvent.Restore -> {
				viewModelScope.launch {
					noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
					recentlyDeletedNote = null
					_eventFlow.emit(UiEvents.ShowSnackBar(show = false))
				}
			}
			NotesEvent.ToggleOrderSection -> {
				viewModelScope.launch {
					_state.emit(state.value.copy(isOrderSectionVisible = !state.value.isOrderSectionVisible))
				}
			}
		}
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