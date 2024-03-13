package com.looker.notesy.data.repository

import com.looker.notesy.data.data_source.NoteDao
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> = dao.getAllNotes()

    override fun getNoteById(id: Int): Flow<Note?> = dao.getNoteById(id)

    override fun getLastNoteId(): Flow<Int?> = dao.getLastNoteId()

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}