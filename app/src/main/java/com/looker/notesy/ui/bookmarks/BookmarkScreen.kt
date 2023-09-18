package com.looker.notesy.ui.bookmarks

import android.text.format.DateUtils
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.looker.notesy.R
import com.looker.notesy.domain.model.Bookmark
import com.looker.notesy.ui.add_edit_note.components.TransparentTextField
import com.looker.notesy.ui.components.NotesyImageWithAmbience
import com.looker.notesy.ui.notes.components.DeleteDialog
import com.looker.notesy.ui.theme.bottom
import com.looker.notesy.ui.theme.top
import com.looker.notesy.ui.utils.LocalSpacing
import com.looker.notesy.ui.utils.plus
import com.looker.notesy.util.favIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
	viewModel: BookmarkViewModel = hiltViewModel(),
	initial: String? = null
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
			items(bookmarks) { bookmark ->
				val dismissState = rememberDismissState(
					confirmValueChange = {
						if (it != DismissValue.Default) viewModel.showDeleteDialog(bookmark)
						true
					}
				)
				LaunchedEffect(viewModel.deletedBookmark) {
					if (viewModel.deletedBookmark != null) dismissState.reset()
				}
				BookmarkItem(
					bookmark = bookmark,
					dismissState = dismissState,
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookmarkItem(
	bookmark: Bookmark,
	dismissState: DismissState,
	modifier: Modifier = Modifier
) {
	val clipboardManager = LocalClipboardManager.current
	val uriHandler = LocalUriHandler.current
	SwipeToDismiss(
		modifier = modifier
			.fillMaxWidth()
			.clip(MaterialTheme.shapes.large)
			.combinedClickable(
				onClick = { uriHandler.openUri(bookmark.url) },
				onLongClick = { clipboardManager.setText(AnnotatedString(bookmark.url)) }
			),
		state = dismissState,
		background = {
			val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
			val alignment by remember(direction) {
				derivedStateOf {
					when (direction) {
						DismissDirection.StartToEnd -> Alignment.CenterStart
						DismissDirection.EndToStart -> Alignment.CenterEnd
					}
				}
			}
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.errorContainer)
					.absolutePadding(left = 10.dp, right = 10.dp),
				contentAlignment = alignment
			) {
				Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
			}
		},
		dismissContent = {
			Surface(
				modifier = Modifier.fillMaxWidth(),
				shape = MaterialTheme.shapes.large,
				border = BorderStroke(
					width = 1.dp,
					color = MaterialTheme.colorScheme.outlineVariant
				)
			) {
				Row(
					modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
					verticalAlignment = Alignment.CenterVertically
				) {
					val artwork = remember(bookmark.artwork) {
						bookmark.artwork.ifBlank { bookmark.url.favIcon() }
					}
					NotesyImageWithAmbience(
						modifier = Modifier.size(48.dp),
						imageModifier = Modifier.clip(CircleShape),
						data = artwork
					)
					Column {
						Spacer(modifier = Modifier.height(8.dp))
						Text(
							modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
							text = bookmark.name,
							style = MaterialTheme.typography.titleMedium,
							fontWeight = FontWeight.SemiBold,
							maxLines = 3
						)
						Spacer(modifier = Modifier.height(4.dp))
						Text(
							modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
							text = bookmark.url,
							style = MaterialTheme.typography.labelMedium,
							color = MaterialTheme.colorScheme.outline,
							maxLines = 1
						)
						Spacer(modifier = Modifier.height(12.dp))
						val bookmarkTimestamp = remember(bookmark.lastModified) {
							bookmark.lastModified.relativeTimePassedString()
						}
						Text(
							modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
							text = stringResource(R.string.label_last_modified) + " " + bookmarkTimestamp,
							style = MaterialTheme.typography.labelMedium,
							color = MaterialTheme.colorScheme.outline
						)
						Spacer(modifier = Modifier.height(8.dp))
					}
				}
			}
		}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookmarkDialog(
	modifier: Modifier = Modifier,
	url: String,
	onUrlChange: (String) -> Unit,
	onDismiss: () -> Unit,
	onConfirm: () -> Unit
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
			containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
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