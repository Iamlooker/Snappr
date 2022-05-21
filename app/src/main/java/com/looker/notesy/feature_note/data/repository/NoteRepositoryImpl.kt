package com.looker.notesy.feature_note.data.repository

import com.looker.notesy.feature_note.data.data_source.NoteDao
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
	private val dao: NoteDao
): NoteRepository {
	override fun getNotes(): Flow<List<Note>> = dao.getAllNotes()

	override suspend fun getNoteById(id: Int): Note? = dao.getNoteById(id)

	override suspend fun insertNote(note: Note) {
		dao.insertNote(note)
	}

	override suspend fun deleteNote(note: Note) {
		dao.deleteNote(note)
	}
}