package com.looker.notesy.ui.notes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.notesy.ui.notes.NotesScreen

const val NOTES_SCREEN_ROUTE = "notes_screen"

fun NavController.navigateToNotes(navOptions: NavOptions? = null) {
    navigate(NOTES_SCREEN_ROUTE, navOptions)
}

fun NavGraphBuilder.notesScreen(
    onNoteClick: (Int?) -> Unit,
    onCreateNew: () -> Unit
) {
    composable(route = NOTES_SCREEN_ROUTE) {
        NotesScreen(
            onNoteClick = onNoteClick,
            onCreateNewClick = onCreateNew
        )
    }
}