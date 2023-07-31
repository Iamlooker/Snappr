package com.looker.notesy.data.repository

import com.looker.notesy.data.data_source.BookmarkDao
import com.looker.notesy.domain.model.Bookmark
import com.looker.notesy.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow

class BookmarkRepositoryImpl(private val dao: BookmarkDao) : BookmarkRepository {

	override fun getAllBookmarkStream(): Flow<List<Bookmark>> = dao.getAllBookmarkStream()

	override fun getBookmarkStream(id: Long): Flow<Bookmark?> = dao.getBookmarkStream(id)

	override suspend fun deleteBookmark(bookmark: Bookmark) = dao.deleteBookmark(bookmark)

	override suspend fun upsertBookmark(bookMark: Bookmark) = dao.upsertBookmark(bookMark)

}