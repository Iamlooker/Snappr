package com.looker.notesy.domain.repository

import com.looker.notesy.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

	fun getNotes() : Flow<List<Note>>

	fun getNoteById(id: Int): Flow<Note?>

	fun getLastNoteId(): Flow<Int?>

	suspend fun insertNote(note: Note)

	suspend fun deleteNote(note: Note)
}