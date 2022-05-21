package com.looker.notesy.feature_note.domain.use_case

import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.repository.NoteRepository
import com.looker.notesy.feature_note.domain.utils.NoteOrder
import com.looker.notesy.feature_note.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(private val repository: NoteRepository) {

	operator fun invoke(
		noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
	): Flow<List<Note>> {
		return repository.getNotes().map { notes ->
			when(noteOrder.orderType) {
				is OrderType.Ascending -> {
					when(noteOrder) {
						is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
						is NoteOrder.Date -> notes.sortedBy { it.timeCreated }
					}
				}
				is OrderType.Descending -> {
					when(noteOrder) {
						is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
						is NoteOrder.Date -> notes.sortedByDescending { it.timeCreated }
					}
				}
			}
		}
	}
}