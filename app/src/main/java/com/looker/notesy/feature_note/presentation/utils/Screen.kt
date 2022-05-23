package com.looker.notesy.feature_note.presentation.utils

import com.looker.notesy.feature_note.presentation.utils.Utils.ADD_EDIT_SCREEN_ROUTE
import com.looker.notesy.feature_note.presentation.utils.Utils.NOTES_SCREEN_ROUTE

sealed class Screen(val route: String) {
	object NotesScreen : Screen(NOTES_SCREEN_ROUTE)
	object AddEditNoteScreen : Screen(ADD_EDIT_SCREEN_ROUTE)
}