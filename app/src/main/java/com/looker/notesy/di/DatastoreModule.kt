package com.looker.notesy.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.looker.notesy.data.data_source.AppSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val PREFERENCES = "notesy_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

	@Singleton
	@Provides
	fun provideDatastore(
		@ApplicationContext context: Context
	): DataStore<Preferences> = PreferenceDataStoreFactory.create {
		context.preferencesDataStoreFile(PREFERENCES)
	}

	@Singleton
	@Provides
	fun provideUserPreferencesRepository(
		dataStore: DataStore<Preferences>
	): AppSettingsRepository = AppSettingsRepository(dataStore)
}