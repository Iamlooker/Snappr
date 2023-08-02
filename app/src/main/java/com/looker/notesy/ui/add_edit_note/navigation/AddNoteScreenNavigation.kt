package com.looker.notesy.ui.add_edit_note.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.looker.notesy.ui.add_edit_note.AddEditNoteScreen

const val ADD_EDIT_SCREEN_ROUTE = "add_edit_note_screen"

const val NOTE_ID_ARG = "noteId"

fun NavController.navigateToAddScreen(noteId: Int? = null, navOptions: NavOptions? = null) {
    navigate("$ADD_EDIT_SCREEN_ROUTE?$NOTE_ID_ARG=${noteId ?: -1}", navOptions)
}

fun NavGraphBuilder.addEditNoteScreen(
    onBackPressed: () -> Unit
) {
    composable(
        route = "$ADD_EDIT_SCREEN_ROUTE?noteId={$NOTE_ID_ARG}",
        arguments = listOf(
            navArgument(NOTE_ID_ARG) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        AddEditNoteScreen(navigateUp = onBackPressed)
    }
}