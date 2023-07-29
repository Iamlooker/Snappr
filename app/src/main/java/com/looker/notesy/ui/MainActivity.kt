package com.looker.notesy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.looker.notesy.ui.add_edit_note.navigation.addEditNoteScreen
import com.looker.notesy.ui.add_edit_note.navigation.navigateToAddScreen
import com.looker.notesy.ui.notes.navigation.NOTES_SCREEN_ROUTE
import com.looker.notesy.ui.notes.navigation.notesScreen
import com.looker.notesy.ui.notes_detail.components.navigateToDetailScreen
import com.looker.notesy.ui.notes_detail.components.notesDetailScreen
import com.looker.notesy.ui.theme.NotesyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		WindowCompat.setDecorFitsSystemWindows(window, false)
		super.onCreate(savedInstanceState)
		setContent {
			NotesyTheme {
				Notesy()
			}
		}
	}
}

@Composable
fun Notesy() {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colorScheme.background
	) {
		val navController = rememberNavController()
		NavHost(
			navController = navController,
			startDestination = NOTES_SCREEN_ROUTE
		) {
			notesScreen(
				onNoteClick = navController::navigateToDetailScreen,
				onCreateNew = navController::navigateToAddScreen
			)
			addEditNoteScreen(navController::popBackStack)
			notesDetailScreen(
				onBackPressed = navController::popBackStack,
				onEditClicked = navController::navigateToAddScreen,
				onNavigateToNote = navController::navigateToDetailScreen
			)
		}
	}
}