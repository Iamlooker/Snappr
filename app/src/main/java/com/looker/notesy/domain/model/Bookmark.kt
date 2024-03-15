package com.looker.notesy.domain.model

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.notesy.domain.model.BookmarkContentType.FavIcon
import com.looker.notesy.domain.model.BookmarkContentType.PreviewImage

@Entity
@Stable
data class Bookmark(
    val url: String,
    val name: String,
    val favIcon: String,
    val lastModified: Long,
    val previewImage: String? = null,
    @PrimaryKey
    val id: Long? = null,
) {
    val contentType: BookmarkContentType
        get() = if (previewImage != null) PreviewImage
        else FavIcon

}

enum class BookmarkContentType { PreviewImage, FavIcon }
