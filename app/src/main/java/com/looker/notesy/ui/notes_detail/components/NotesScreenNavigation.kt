package com.looker.notesy.ui.notes_detail.components

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.looker.notesy.ui.add_edit_note.AddEditNoteScreen
import com.looker.notesy.ui.notes_detail.NotesDetailScreen

const val NOTES_DETAIL_SCREEN_ROUTE = "notes_detail_screen"

fun NavController.navigateToDetailScreen(noteId: Int? = null, navOptions: NavOptions? = null) {
	navigate("$NOTES_DETAIL_SCREEN_ROUTE?noteId=${noteId ?: -1}", navOptions)
}

fun NavGraphBuilder.notesDetailScreen(
	onBackPressed: () -> Unit,
	onEditClicked: (Int) -> Unit,
	onNavigateToNote: (Int) -> Unit
) {
	composable(
		route = "$NOTES_DETAIL_SCREEN_ROUTE?noteId={noteId}",
		arguments = listOf(
			navArgument(name = "noteId") {
				type = NavType.IntType
				defaultValue = -1
			}
		)
	) {
		NotesDetailScreen(onBackPressed = onBackPressed, onEditClicked = onEditClicked, onNavigateToNote = onNavigateToNote)
	}
}