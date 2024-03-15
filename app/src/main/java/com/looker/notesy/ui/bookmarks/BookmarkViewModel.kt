package com.looker.notesy.ui.bookmarks

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.looker.notesy.domain.model.Bookmark
import com.looker.notesy.domain.repository.BookmarkRepository
import com.looker.notesy.ui.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: BookmarkRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val deeplinkIntent: Intent? = savedStateHandle[NavController.KEY_DEEP_LINK_INTENT]
    private val url: String? = deeplinkIntent?.getStringExtra(Intent.EXTRA_TEXT)

    val bookmarks = repository
        .getAllBookmarkStream()
        .asStateFlow(initial = emptyList())

    var isAddBookmarkDialogOpen: Boolean by mutableStateOf(false)
        private set

    var deletedBookmark: Bookmark? by mutableStateOf(null)
        private set

    var bookmarkUrl: String by mutableStateOf("")
        private set

    fun setUrl(url: String) {
        bookmarkUrl = url
    }

    fun addBookmark() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = bookmarkUrl
            hideAddBookmarkDialog()
            if (url.isNotBlank()) {
                repository.insertBookmark(url)
            }
        }
    }

    fun showDeleteDialog(bookmark: Bookmark? = null) {
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

    init {
        if (url != null) {
            showAddBookmarkDialog()
            setUrl(url)
        }
    }

}
