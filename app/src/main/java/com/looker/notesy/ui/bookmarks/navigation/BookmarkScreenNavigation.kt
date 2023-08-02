package com.looker.notesy.ui.bookmarks.navigation

import android.content.Intent
import android.os.Build
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.looker.notesy.ui.bookmarks.BookmarkScreen

const val BOOKMARKS_SCREEN_ROUTE = "bookmarks_screen"

private val linkBasedDeepLinks = listOf(
	navDeepLink {
		mimeType = "text/plain"
		action = Intent.ACTION_SEND
	}
)

fun NavController.navigateToBookmarks(navOptions: NavOptions? = null) {
	navigate(BOOKMARKS_SCREEN_ROUTE, navOptions)
}

fun NavGraphBuilder.bookmarksScreen() {
	composable(
		route = BOOKMARKS_SCREEN_ROUTE,
		deepLinks = linkBasedDeepLinks
	) { backStackEntry ->
		val intent = remember {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				backStackEntry.arguments?.getParcelable(
					NavController.KEY_DEEP_LINK_INTENT,
					Intent::class.java
				)
			} else {
				backStackEntry.arguments?.getParcelable(
					NavController.KEY_DEEP_LINK_INTENT
				) as? Intent
			}
		}
		BookmarkScreen(initial = intent?.getStringExtra(Intent.EXTRA_TEXT))
	}
}