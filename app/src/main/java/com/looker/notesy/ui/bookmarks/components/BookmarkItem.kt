package com.looker.notesy.ui.bookmarks.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.looker.notesy.domain.model.Bookmark
import com.looker.notesy.ui.bookmarks.relativeTimePassedString
import com.looker.notesy.ui.components.NotesyImageWithAmbience
import com.looker.notesy.ui.theme.NotesyTheme
import com.looker.notesy.ui.utils.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkItem(
    bookmark: Bookmark,
    dismissState: SwipeToDismissBoxState,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge),
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val alignment by remember(direction) {
                derivedStateOf {
                    when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        SwipeToDismissBoxValue.Settled -> Alignment.Center
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .absolutePadding(
                        left = LocalSpacing.current.border,
                        right = LocalSpacing.current.border
                    ),
                contentAlignment = alignment
            ) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
            }
        },
        content = {
            val timestamp = remember(bookmark.lastModified) {
                bookmark.lastModified.relativeTimePassedString()
            }
            if (bookmark.previewImage != null) {
                LargeBookmarkItem(
                    image = bookmark.previewImage,
                    name = bookmark.name,
                    url = bookmark.url,
                    timestamp = timestamp,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
            } else {
                SmallBookmarkItem(
                    image = bookmark.favIcon,
                    name = bookmark.name,
                    url = bookmark.url,
                    timestamp = timestamp,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
            }
        }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LargeBookmarkItem(
    image: String?,
    name: String,
    url: String,
    timestamp: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column() {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .padding(3.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .fillMaxWidth()
                    .aspectRatio(16F / 9F)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
                text = url,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
                text = timestamp,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SmallBookmarkItem(
    image: String?,
    name: String,
    url: String,
    timestamp: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier = Modifier.padding(LocalSpacing.current.border),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NotesyImageWithAmbience(
                modifier = Modifier.size(56.dp),
                imageModifier = Modifier.clip(CircleShape),
                data = image
            )
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
                    text = url,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.border),
                    text = timestamp,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun LargeItemPreview() {
    NotesyTheme {
        LargeBookmarkItem(
            url = "https://looker.github.io",
            name = "Portfolio",
            image = "",
            timestamp = (System.currentTimeMillis() - 120_000).relativeTimePassedString(),
            onClick = { /*TODO*/ },
            onLongClick = { /*TODO*/ },
        )
    }
}

@Preview
@Composable
private fun SmallItemPreview() {
    NotesyTheme {
        SmallBookmarkItem(
            url = "https://looker.github.io",
            name = "Portfolio",
            image = "",
            timestamp = (System.currentTimeMillis() - 120_000).relativeTimePassedString(),
            onClick = { /*TODO*/ },
            onLongClick = { /*TODO*/ },
        )
    }
}
