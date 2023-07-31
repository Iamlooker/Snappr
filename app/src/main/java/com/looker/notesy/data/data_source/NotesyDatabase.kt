package com.looker.notesy.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.looker.notesy.domain.model.Bookmark
import com.looker.notesy.domain.model.Note

@Database(
	entities = [Note::class, Bookmark::class],
	version = 1
)
abstract class NotesyDatabase : RoomDatabase() {
	abstract val noteDao: NoteDao
	abstract val bookmarkDao: BookmarkDao

	companion object {
		const val DATABASE = "notes_db"
	}
}