package com.looker.notesy.ui.bookmarks

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.notesy.R
import com.looker.notesy.ui.add_edit_note.components.TransparentTextField
import com.looker.notesy.ui.bookmarks.components.BookmarkItem
import com.looker.notesy.ui.notes.components.DeleteDialog
import com.looker.notesy.ui.theme.bottom
import com.looker.notesy.ui.theme.top
import com.looker.notesy.ui.utils.LocalSpacing
import com.looker.notesy.ui.utils.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    initial: String? = null,
) {
    LaunchedEffect(initial) {
        viewModel.setPrimaryUrl(initial)
    }
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = viewModel::showAddBookmarkDialog
            ) {
                Icon(imageVector = Icons.Rounded.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(LocalSpacing.current.text))
                Text(text = stringResource(R.string.action_add_bookmark))
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        if (viewModel.isAddBookmarkDialogOpen) {
            AddBookmarkDialog(
                url = viewModel.bookmarkUrl,
                onUrlChange = viewModel::setUrl,
                onDismiss = viewModel::hideAddBookmarkDialog,
                onConfirm = viewModel::addBookmark
            )
        }
        val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
        LazyColumn(
            contentPadding = paddingValues + PaddingValues(top = 12.dp, bottom = 84.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = bookmarks,
                key = { it.id!! },
                contentType = { it.contentType }
            ) { bookmark ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it != SwipeToDismissBoxValue.Settled) viewModel.showDeleteDialog(
                            bookmark
                        )
                        true
                    }
                )
                val clipboardManager = LocalClipboardManager.current
                val uriHandler = LocalUriHandler.current
                LaunchedEffect(viewModel.deletedBookmark) {
                    if (viewModel.deletedBookmark != null) dismissState.reset()
                }
                BookmarkItem(
                    bookmark = bookmark,
                    dismissState = dismissState,
                    onClick = { uriHandler.openUri(bookmark.url) },
                    onLongClick = { clipboardManager.setText(AnnotatedString(bookmark.url)) },
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.border)
                )
            }
        }
        if (viewModel.deletedBookmark != null) {
            DeleteDialog(
                name = viewModel.deletedBookmark!!.name,
                onConfirm = viewModel::confirmDelete,
                onDismiss = viewModel::showDeleteDialog
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookmarkDialog(
    url: String,
    onUrlChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.action_add_bookmark),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        TransparentTextField(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            text = url,
            onValueChange = onUrlChange,
            hint = stringResource(R.string.label_url_hint),
            shape = MaterialTheme.shapes.large.top(),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
        Spacer(modifier = Modifier.height(4.dp))
        FilledTonalButton(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large.bottom(),
            contentPadding = PaddingValues(16.dp),
            onClick = onConfirm
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(LocalSpacing.current.text))
            Text(text = stringResource(R.string.action_add))
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

fun Long.relativeTimePassedString(): String {
    val currentTimeMillis = System.currentTimeMillis()
    return DateUtils.getRelativeTimeSpanString(
        this,
        currentTimeMillis,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()
}
