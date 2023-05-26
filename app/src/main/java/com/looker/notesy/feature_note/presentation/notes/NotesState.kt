package com.looker.notesy.feature_note.presentation.notes

import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.utils.NoteOrder
import com.looker.notesy.feature_note.domain.utils.OrderType

data class NotesState(
	val notesList: List<Note> = listOf(),
	val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
	val isOrderSectionVisible: Boolean = false,
	val showDeleteDialog: Note? = null
)
