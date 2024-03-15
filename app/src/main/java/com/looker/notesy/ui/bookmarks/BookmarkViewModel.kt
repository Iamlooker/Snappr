package com.looker.notesy.ui.bookmarks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.domain.model.Bookmark
import com.looker.notesy.domain.repository.BookmarkRepository
import com.looker.notesy.ui.utils.asStateFlow
import com.looker.notesy.util.UrlParser
import com.looker.notesy.util.favIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: BookmarkRepository,
    private val urlParser: UrlParser,
) : ViewModel() {

    private var initialUrl: String? by mutableStateOf(null)

    val bookmarks = repository
        .getAllBookmarkStream()
        .asStateFlow(initial = emptyList())

    var isAddBookmarkDialogOpen by mutableStateOf(false)
        private set

    var deletedBookmark by mutableStateOf<Bookmark?>(null)
        private set

    var isDeletingBookmark by mutableStateOf(false)
        private set

    var bookmarkUrl by mutableStateOf("")
        private set

    fun setPrimaryUrl(url: String?) {
        initialUrl = url
        if (url != null) {
            showAddBookmarkDialog()
            setUrl(url)
        }
    }

    fun setUrl(url: String) {
        bookmarkUrl = url
    }

    fun addBookmark() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = bookmarkUrl
            hideAddBookmarkDialog()
            if (url.isNotBlank()) {
                repository.upsertBookmark(
                    Bookmark(
                        url = url,
                        name = urlParser.getTitle(url),
                        favIcon = url.favIcon(),
                        lastModified = System.currentTimeMillis(),
                        previewImage = urlParser.previewImage()
                    )
                )
            }
        }
    }

    fun showDeleteDialog(bookmark: Bookmark? = null) {
        isDeletingBookmark = true
        deletedBookmark = bookmark
    }

    fun confirmDelete() {
        viewModelScope.launch {
            if (deletedBookmark != null) {
                repository.deleteBookmark(deletedBookmark!!)
                showDeleteDialog()
            }
        }
    }

    fun showAddBookmarkDialog() {
        isAddBookmarkDialogOpen = true
    }

    fun hideAddBookmarkDialog() {
        isAddBookmarkDialogOpen = false
        bookmarkUrl = ""
    }
}
