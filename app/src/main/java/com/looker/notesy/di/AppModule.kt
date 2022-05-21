package com.looker.notesy.di

import android.app.Application
import androidx.room.Room
import com.looker.notesy.feature_note.data.data_source.NoteDatabase
import com.looker.notesy.feature_note.data.repository.NoteRepositoryImpl
import com.looker.notesy.feature_note.domain.repository.NoteRepository
import com.looker.notesy.feature_note.domain.use_case.AddNote
import com.looker.notesy.feature_note.domain.use_case.DeleteNote
import com.looker.notesy.feature_note.domain.use_case.GetNotes
import com.looker.notesy.feature_note.domain.use_case.NoteUseCases
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
	fun provideNoteDatabase(app: Application): NoteDatabase = Room.databaseBuilder(
		app,
		NoteDatabase::class.java,
		NoteDatabase.DATABASE
	).build()

	@Provides
	@Singleton
	fun providesNoteRepository(db: NoteDatabase): NoteRepository = NoteRepositoryImpl(db.noteDao)

	@Provides
	@Singleton
	fun provideNoteUseCases(repository: NoteRepository) = NoteUseCases(
		getNotes = GetNotes(repository),
		deleteNote = DeleteNote(repository),
		addNote = AddNote(repository)
	)
}