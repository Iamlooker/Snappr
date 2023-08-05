package com.looker.notesy.ui.notes

import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.utils.NoteOrder
import com.looker.notesy.domain.utils.OrderType

data class NotesState(
	val notesList: List<Note> = listOf(),
	val noteOrder: NoteOrder = NoteOrder.Id(OrderType.Descending),
	val showDeleteDialog: Note? = null,
	val snackBarMessage: String? = null
)
