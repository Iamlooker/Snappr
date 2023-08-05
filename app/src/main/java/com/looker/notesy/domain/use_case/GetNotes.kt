package com.looker.notesy.domain.use_case

import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository
import com.looker.notesy.domain.utils.NoteOrder
import com.looker.notesy.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetNotes(private val repository: NoteRepository) {

	operator fun invoke(): Flow<List<Note>> {
		return repository.getNotes()
	}

	suspend fun contains(id: Int?): Boolean {
		return repository.getNotes().first().any { it.id == id }
	}
}

fun List<Note>.noteOrder(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)) =
	when (noteOrder.orderType) {
		OrderType.Ascending -> {
			when (noteOrder) {
				is NoteOrder.Title -> sortedBy { it.titleOrContent.lowercase() }
				is NoteOrder.Date -> sortedBy { it.timeCreated }
				is NoteOrder.Id -> sortedBy { it.id }
			}
		}

		OrderType.Descending -> {
			when (noteOrder) {
				is NoteOrder.Title -> sortedByDescending { it.titleOrContent.lowercase() }
				is NoteOrder.Date -> sortedByDescending { it.timeCreated }
				is NoteOrder.Id -> sortedByDescending { it.id }
			}
		}
	}

private val Note.titleOrContent get() = title.ifEmpty { content }