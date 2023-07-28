package com.looker.notesy.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.looker.notesy.domain.model.Note

@Database(
	entities = [Note::class],
	version = 1
)
abstract class NotesyDatabase: RoomDatabase() {
	abstract val noteDao: NoteDao

	companion object {
		const val DATABASE = "notes_db"
	}
}