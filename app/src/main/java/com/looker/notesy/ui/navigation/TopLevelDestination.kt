package com.looker.notesy.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.notesy.R

enum class TopLevelDestination(
	val icon: ImageVector,
	@StringRes val title: Int
) {
	Notes(Icons.Rounded.Notes, R.string.app_name),
	Bookmarks(Icons.Rounded.Bookmarks, R.string.label_bookmarks)
}