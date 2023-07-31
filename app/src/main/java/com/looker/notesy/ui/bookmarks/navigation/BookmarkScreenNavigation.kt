package com.looker.notesy.ui.bookmarks.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.looker.notesy.ui.bookmarks.BookmarkScreen

const val BOOKMARKS_SCREEN_ROUTE = "bookmarks_screen"

fun NavController.navigateToBookmarks(navOptions: NavOptions? = null) {
	navigate(BOOKMARKS_SCREEN_ROUTE, navOptions)
}

fun NavGraphBuilder.bookmarksScreen() {
	composable(route = BOOKMARKS_SCREEN_ROUTE) {
		BookmarkScreen()
	}
}