package com.looker.notesy.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.notesy.R

enum class TopLevelDestination(
    val icon: ImageVector,
    @StringRes val title: Int
) {
    Notes(Icons.AutoMirrored.Rounded.Notes, R.string.app_name),
    Bookmarks(Icons.Rounded.BookmarkBorder, R.string.label_bookmarks)
}