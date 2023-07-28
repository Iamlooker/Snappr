package com.looker.notesy.domain.use_case

import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository

class GetNote(private val repository: NoteRepository) {
	suspend operator fun invoke(id: Int): Note? {
		return repository.getNoteById(id)
	}
}