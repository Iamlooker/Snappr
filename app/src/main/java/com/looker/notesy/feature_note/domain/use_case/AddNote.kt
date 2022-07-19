package com.looker.notesy.feature_note.domain.use_case

import com.looker.notesy.feature_note.domain.model.InvalidNoteException
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.repository.NoteRepository

class AddNote(private val repository: NoteRepository) {

	@Throws(InvalidNoteException::class)
	suspend operator fun invoke(note: Note) {
		if (note.content.isBlank() && note.title.isBlank()) throw InvalidNoteException("Note is blank.")
		repository.insertNote(note)
	}
}