package com.looker.notesy.data.data_source

import androidx.room.*
import com.looker.notesy.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

	@Query("SELECT * FROM Bookmark ORDER BY lastModified DESC")
	fun getAllBookmarkStream(): Flow<List<Bookmark>>

	@Query("SELECT * FROM Bookmark WHERE id = :id")
	fun getBookmarkStream(id: Long): Flow<Bookmark?>

	@Delete
	suspend fun deleteBookmark(bookmark: Bookmark)

	@Upsert
	suspend fun upsertBookmark(bookMark: Bookmark)

}