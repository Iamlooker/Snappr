package com.looker.notesy.domain.repository

import com.looker.notesy.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun getAllBookmarkStream(): Flow<List<Bookmark>>

    fun getBookmarkStream(id: Long): Flow<Bookmark?>

    suspend fun deleteBookmark(bookmark: Bookmark)

    suspend fun upsertBookmark(bookMark: Bookmark)

}