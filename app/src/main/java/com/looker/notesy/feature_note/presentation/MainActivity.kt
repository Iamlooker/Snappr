package com.looker.notesy.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.looker.notesy.feature_note.data.data_source.NoteDao
import com.looker.notesy.feature_note.data.data_source.NoteDatabase
import com.looker.notesy.feature_note.domain.model.Note
import com.looker.notesy.feature_note.presentation.notes.NotesScreen
import com.looker.notesy.ui.theme.NotesyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	@Inject
	lateinit var noteDb: NoteDatabase
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		lifecycleScope.launch {
			noteDb.noteDao.getAllNotes().collectLatest {
				if (it.isEmpty()) {
					noteDb.noteDao.insertNote(
						Note(
							title = "Long Note",
							content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam vitae facilisis tellus. Ut id lacus non tortor porttitor aliquam. Maecenas mattis diam odio, at venenatis tellus commodo vitae. Aenean sed ex ut nisi malesuada elementum vel sed velit. Fusce iaculis urna sagittis velit maximus, eget iaculis nisi aliquet. Phasellus ac condimentum mauris. Sed non sem orci. Aliquam lacinia dictum congue. Phasellus molestie lacus nisi, ac ultrices magna aliquam in.",
							timeCreated = 0L,
							color = 0
						)
					)
					noteDb.noteDao.insertNote(
						Note(
							title = "Short Note",
							content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
							timeCreated = 1L,
							color = 0
						)
					)
				}
			}
		}
		setContent {
			NotesyTheme(dynamicColor = false) {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					NotesScreen(rememberNavController())
				}
			}
		}
	}
}