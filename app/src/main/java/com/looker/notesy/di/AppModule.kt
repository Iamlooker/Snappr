package com.looker.notesy.di

import android.app.Application
import androidx.room.Room
import com.looker.notesy.data.data_source.NotesyDatabase
import com.looker.notesy.data.repository.BookmarkRepositoryImpl
import com.looker.notesy.data.repository.NoteRepositoryImpl
import com.looker.notesy.domain.repository.BookmarkRepository
import com.looker.notesy.domain.repository.NoteRepository
import com.looker.notesy.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun provideNoteDatabase(app: Application): NotesyDatabase = Room.databaseBuilder(
		app,
		NotesyDatabase::class.java,
		NotesyDatabase.DATABASE
	).build()

	@Provides
	@Singleton
	fun providesNoteRepository(db: NotesyDatabase): NoteRepository = NoteRepositoryImpl(db.noteDao)

	@Provides
	@Singleton
	fun providesBookmarkRepository(db: NotesyDatabase): BookmarkRepository = BookmarkRepositoryImpl(db.bookmarkDao)

	@Provides
	@Singleton
	fun provideNoteUseCases(repository: NoteRepository) = NoteUseCases(
		getNotes = GetNotes(repository),
		deleteNote = DeleteNote(repository),
		addNote = AddNote(repository),
		getNote = GetNote(repository),
		getLastId = GetLastId(repository)
	)
}