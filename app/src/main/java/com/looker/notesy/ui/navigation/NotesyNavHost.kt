package com.looker.notesy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import com.looker.notesy.ui.NotesyAppState
import com.looker.notesy.ui.add_edit_note.navigation.addEditNoteScreen
import com.looker.notesy.ui.add_edit_note.navigation.navigateToAddScreen
import com.looker.notesy.ui.bookmarks.navigation.bookmarksScreen
import com.looker.notesy.ui.notes.navigation.NOTES_SCREEN_ROUTE
import com.looker.notesy.ui.notes.navigation.notesScreen
import com.looker.notesy.ui.notes_detail.navigation.navigateToDetailScreen
import com.looker.notesy.ui.notes_detail.navigation.notesDetailScreen

@Composable
fun NotesyNavHost(
	appState: NotesyAppState,
	modifier: Modifier = Modifier,
	startDestination: String = NOTES_SCREEN_ROUTE
) {
	val navController = appState.navController
	NavHost(
		navController = navController,
		startDestination = startDestination,
		modifier = modifier
	) {
		bookmarkGraph()
		notesGraph(navController)
	}
}

fun NavGraphBuilder.bookmarkGraph() {
	bookmarksScreen()
}

fun NavGraphBuilder.notesGraph(navController: NavController) {
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