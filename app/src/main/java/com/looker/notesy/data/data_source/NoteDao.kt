package com.looker.notesy.data.data_source

import androidx.room.*
import com.looker.notesy.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

	@Query("SELECT * FROM note")
	fun getAllNotes(): Flow<List<Note>>

	@Query("SELECT * FROM note WHERE id = :id")
	suspend fun getNoteById(id: Int): Note?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertNote(note: Note)

	@Delete
	suspend fun deleteNote(note: Note)
}