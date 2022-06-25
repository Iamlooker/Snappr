package com.looker.notesy.feature_note.presentation.notes

import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.utils.NoteOrder

sealed class NotesEvent {
	data class Order(val noteOrder: NoteOrder) : NotesEvent()
	data class Delete(val note: Note) : NotesEvent()
	object DeleteConfirmation : NotesEvent()
	object RemoveDeleteConfirmation : NotesEvent()
	object Restore : NotesEvent()
	object ToggleOrderSection : NotesEvent()
}
