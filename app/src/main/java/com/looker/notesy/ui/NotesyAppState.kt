package com.looker.notesy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.looker.notesy.ui.bookmarks.navigation.BOOKMARKS_SCREEN_ROUTE
import com.looker.notesy.ui.bookmarks.navigation.navigateToBookmarks
import com.looker.notesy.ui.navigation.TopLevelDestination
import com.looker.notesy.ui.notes.navigation.NOTES_SCREEN_ROUTE
import com.looker.notesy.ui.notes.navigation.navigateToNotes

@Composable
fun rememberNotesyAppState(
	navController: NavHostController = rememberNavController()
): NotesyAppState = remember(navController) {
	NotesyAppState(navController)
}

@Stable
class NotesyAppState(val navController: NavHostController) {

	val currentDestination: NavDestination?
		@Composable get() = navController
			.currentBackStackEntryAsState().value?.destination

	val currentTopLevelDestination: TopLevelDestination?
		@Composable get() = when (currentDestination?.route) {
			BOOKMARKS_SCREEN_ROUTE -> TopLevelDestination.Bookmarks
			NOTES_SCREEN_ROUTE -> TopLevelDestination.Notes
			else -> null
		}

	val isTopLevelDestination: Boolean
		@Composable get() = currentTopLevelDestination != null

	val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

	fun navigateToTopLevelDestination(destination: TopLevelDestination) {
		val topLevelNavOptions = navOptions {
			popUpTo(navController.graph.findStartDestination().id) {
				saveState = true
			}
			launchSingleTop = true
			restoreState = true
		}
		when (destination) {
			TopLevelDestination.Notes -> navController.navigateToNotes(topLevelNavOptions)
			TopLevelDestination.Bookmarks -> navController.navigateToBookmarks(topLevelNavOptions)
		}
	}
}