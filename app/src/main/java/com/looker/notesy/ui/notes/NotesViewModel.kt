package com.looker.notesy.ui.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.data.data_source.AppSettingsRepository
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.use_case.NoteUseCases
import com.looker.notesy.domain.use_case.noteOrder
import com.looker.notesy.domain.utils.NoteOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
	private val noteUseCases: NoteUseCases,
	private val settings: AppSettingsRepository
) : ViewModel() {

	private val deleteConfirmation = MutableStateFlow<Note?>(null)
	private val snackBarMessage = MutableStateFlow<String?>(null)

	val noteState = combine(
		noteUseCases.getNotes(),
		deleteConfirmation,
		snackBarMessage,
		settings.stream
	) { notes, deleteDialog, snackBarMessage, settings ->
		Log.e("tag", notes.toString())
		NotesState(
			notesList = notes.noteOrder(settings.noteOrder),
			noteOrder = settings.noteOrder,
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
		viewModelScope.launch { deleteConfirmation.emit(note) }
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
		viewModelScope.launch { settings.setNoteOrder(order) }
	}

	fun snackBarConsumed() {
		viewModelScope.launch { snackBarMessage.emit(null) }
	}
}