package com.looker.notesy.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.looker.notesy.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteById(id: Int): Flow<Note?>

    @Query("SELECT id FROM note ORDER BY id DESC LIMIT 1")
    fun getLastNoteId(): Flow<Int?>

    @Upsert
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}