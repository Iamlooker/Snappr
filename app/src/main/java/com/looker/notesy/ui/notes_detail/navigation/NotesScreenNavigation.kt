package com.looker.notesy.ui.notes_detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        NotesDetailScreen(
            onBackPressed = onBackPressed,
            onEditClicked = onEditClicked,
            onNavigateToNote = onNavigateToNote
        )
    }
}