package com.looker.notesy.ui.notes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.notesy.domain.model.Note
import com.looker.notesy.ui.components.FormattedNoteContent
import com.looker.notesy.ui.components.NoteId
import com.looker.notesy.ui.theme.NotesyTheme
import com.looker.notesy.ui.utils.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    state: SwipeToDismissBoxState = rememberSwipeToDismissBoxState(),
    onNoteClick: () -> Unit = {}
) {
    SwipeToDismissBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onNoteClick),
        state = state,
        backgroundContent = {
            val direction = state.dismissDirection
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
                    .absolutePadding(left = 10.dp, right = 10.dp),
                contentAlignment = alignment
            ) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
            }
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(LocalSpacing.current.border),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    NoteId(
                        text = "# ${note.id}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(LocalSpacing.current.text))
                    if (note.title.isNotBlank()) {
                        FormattedNoteContent(
                            rawText = note.title.trim(),
                            enableSelection = false,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                    if (note.title.isNotBlank() && note.content.isNotBlank()) {
                        Spacer(modifier = Modifier.height(LocalSpacing.current.text))
                    }
                    if (note.content.isNotBlank()) {
                        FormattedNoteContent(
                            rawText = note.content.trim(),
                            enableSelection = false,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 8
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NotePrev() {
    NotesyTheme {
        NoteItem(
            note = Note(
                "Title",
                content = "This is test note, please check it out. ".repeat(20),
                timeCreated = 0L,
            )
        )
    }
}