package com.looker.notesy.feature_note.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.core.UiEvents
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.use_case.NoteUseCases
import com.looker.notesy.feature_note.domain.utils.NoteOrder
import com.looker.notesy.feature_note.domain.utils.OrderType
import com.looker.notesy.feature_note.presentation.utils.Utils.SNACKBAR_DURATION_LONG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
	private var snackBarJob: Job? = null
	private var deleteConfirmationJob: Job? = null

	init {
		getNotes(NoteOrder.Date(OrderType.Descending))
	}

	fun onEvent(event: NotesEvent) {
		when (event) {
			is NotesEvent.Delete -> {
				viewModelScope.launch {
					snackBarJob?.cancel()
					noteUseCases.deleteNote(event.note)
					recentlyDeletedNote = event.note
					_eventFlow.emit(UiEvents.ShowSnackBar("Restore Note?", true))
					snackBarJob = launch {
						delay(SNACKBAR_DURATION_LONG)
						_eventFlow.emit(UiEvents.ShowSnackBar(show = false))
					}
				}
			}
			is NotesEvent.Order -> {
				if (state.value.noteOrder::class == event.noteOrder::class && state.value.noteOrder.orderType == event.noteOrder.orderType) return
				getNotes(event.noteOrder)
			}
			NotesEvent.DeleteConfirmation -> {
				viewModelScope.launch {
					deleteConfirmationJob?.cancel()
					deleteConfirmationJob = launch {
						_eventFlow.emit(UiEvents.DeleteConfirmation(true))
					}
				}
			}
			NotesEvent.RemoveDeleteConfirmation -> {
				viewModelScope.launch {
					deleteConfirmationJob?.cancel()
					deleteConfirmationJob = launch {
						_eventFlow.emit(UiEvents.DeleteConfirmation(false))
					}
				}
			}
			NotesEvent.Restore -> {
				viewModelScope.launch {
					snackBarJob?.cancel()
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