package com.looker.notesy.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository
import com.looker.notesy.domain.use_case.NoteUseCases
import com.looker.notesy.domain.use_case.noteOrder
import com.looker.notesy.domain.utils.NoteOrder
import com.looker.notesy.domain.utils.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
	private val noteUseCases: NoteUseCases,
	repository: NoteRepository
) : ViewModel() {

	private val sortOrder = MutableStateFlow<NoteOrder>(NoteOrder.Date(OrderType.Descending))
	private val deleteConfirmation = MutableStateFlow<Note?>(null)
	private val snackBarMessage = MutableStateFlow<String?>(null)

	val noteState = combine(
		repository.getNotes(),
		sortOrder,
		deleteConfirmation,
		snackBarMessage
	) { notes, sortOrder, deleteDialog, snackBarMessage ->
		NotesState(
			notesList = notes.noteOrder(sortOrder),
			noteOrder = sortOrder,
			showDeleteDialog = deleteDialog,
			snackBarMessage = snackBarMessage
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		initialValue = NotesState()
	)

	private var recentlyDeletedNote: Note? = null
	private val deletedNoteLock: Mutex = Mutex()

	fun showDeleteDialog(note: Note? = null) {
		viewModelScope.launch {
			deleteConfirmation.emit(note)
		}
	}

	fun deleteNote(note: Note) {
		viewModelScope.launch {
			noteUseCases.deleteNote(note)
			deletedNoteLock.withLock { recentlyDeletedNote = note }
			showDeleteDialog()
			snackBarMessage.emit("Restore Note?")
		}
	}

	fun restoreNote() {
		viewModelScope.launch {
			noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
			deletedNoteLock.withLock { recentlyDeletedNote = null }
		}
	}

	fun reorderNotes(order: NoteOrder) {
		viewModelScope.launch { sortOrder.emit(order) }
	}

	fun snackBarConsumed() {
		viewModelScope.launch { snackBarMessage.emit(null) }
	}
}