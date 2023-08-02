package com.looker.notesy.domain.use_case

import com.looker.notesy.domain.repository.NoteRepository
import kotlinx.coroutines.flow.first

class GetLastId(private val repository: NoteRepository) {
	suspend operator fun invoke(): Int {
		return repository.getLastNoteId().first() ?: 0
	}
}
