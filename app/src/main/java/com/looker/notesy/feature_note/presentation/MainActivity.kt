package com.looker.notesy.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.looker.notesy.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.looker.notesy.feature_note.presentation.notes.NotesScreen
import com.looker.notesy.feature_note.presentation.utils.Screen
import com.looker.notesy.ui.theme.NotesyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			NotesyTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					val navController = rememberNavController()
					NavHost(
						navController = navController,
						startDestination = Screen.NotesScreen.route
					) {
						composable(route = Screen.NotesScreen.route) {
							NotesScreen(navController = navController)
						}
						composable(
							route = Screen.AddEditNoteScreen.route + "?noteId={noteId}",
							arguments = listOf(
								navArgument(name = "noteId") {
									type = NavType.IntType
									defaultValue = -1
								}
							)
						) {
							AddEditNoteScreen(navController = navController)
						}
					}
				}
			}
		}
	}
}