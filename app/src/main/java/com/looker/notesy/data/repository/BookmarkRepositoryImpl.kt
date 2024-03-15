package com.looker.notesy.data.repository

import com.looker.notesy.data.data_source.BookmarkDao
import com.looker.notesy.domain.model.Bookmark
import com.looker.notesy.domain.repository.BookmarkRepository
import com.looker.notesy.util.UrlParser
import com.looker.notesy.util.domain
import com.looker.notesy.util.favIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class BookmarkRepositoryImpl(
    private val dao: BookmarkDao,
    private val scope: CoroutineScope,
) : BookmarkRepository {

    override fun getAllBookmarkStream(): Flow<List<Bookmark>> = dao.getAllBookmarkStream()

    override fun getBookmarkStream(id: Long): Flow<Bookmark?> = dao.getBookmarkStream(id)

    override suspend fun insertBookmark(url: String) {
        val initialBookmark = Bookmark(
            url = url,
            name = url.domain,
            favIcon = url.favIcon(),
            lastModified = System.currentTimeMillis()
        )

        scope.launch {
            val insertedId = dao.upsertBookmark(initialBookmark)
            val insertedBookmark = dao.getBookmarkStream(insertedId).firstOrNull()
            UrlParser.connect(url)
            val title = async {
                UrlParser.getTitle()
            }
            val image = async {
                UrlParser.previewImage()
            }
            if (insertedBookmark != null) {
                dao.upsertBookmark(
                    insertedBookmark.copy(
                        name = title.await() ?: insertedBookmark.name,
                        previewImage = image.await()
                    )
                )
            }
        }
    }

    override suspend fun deleteBookmark(bookmark: Bookmark) {
        dao.deleteBookmark(bookmark)
    }

    override suspend fun upsertBookmark(bookMark: Bookmark) {
        dao.upsertBookmark(bookMark)
    }

}