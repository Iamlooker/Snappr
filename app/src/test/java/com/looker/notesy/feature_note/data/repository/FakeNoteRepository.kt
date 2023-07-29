package com.looker.notesy.feature_note.data.repository

import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeNoteRepository : NoteRepository {

	private val notes = mutableListOf<Note>()

	override fun getNotes(): Flow<List<Note>> = flow { emit(notes) }

	override fun getNoteById(id: Int): Flow<Note?> = getNotes().map { it.find { id == it.id } }

	override suspend fun insertNote(note: Note) {
		notes.add(note)
	}

	override suspend fun deleteNote(note: Note) {
		notes.remove(note)
	}
}