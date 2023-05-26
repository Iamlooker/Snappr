package com.looker.notesy.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.looker.notesy.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.looker.notesy.feature_note.presentation.notes.NotesScreen
import com.looker.notesy.feature_note.presentation.utils.ADD_EDIT_SCREEN_ROUTE
import com.looker.notesy.feature_note.presentation.utils.NOTES_SCREEN_ROUTE
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
			composable(route = NOTES_SCREEN_ROUTE) {
				NotesScreen(
					onNoteClick = {
						navController.navigate("$ADD_EDIT_SCREEN_ROUTE?noteId=${it}")
					},
					onCreateNewClick = {
						navController.navigate(ADD_EDIT_SCREEN_ROUTE)
					}
				)
			}
			composable(
				route = "$ADD_EDIT_SCREEN_ROUTE?noteId={noteId}",
				arguments = listOf(
					navArgument(name = "noteId") {
						type = NavType.IntType
						defaultValue = -1
					}
				)
			) {
				AddEditNoteScreen(navigateUp = navController::popBackStack)
			}
		}
	}
}