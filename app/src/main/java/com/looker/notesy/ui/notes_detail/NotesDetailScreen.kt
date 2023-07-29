package com.looker.notesy.ui.notes_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.looker.notesy.ui.components.FormattedNoteContent
import com.looker.notesy.ui.components.NavigationAppBar
import com.looker.notesy.ui.components.NoteId
import com.looker.notesy.ui.utils.LocalSpacing

@Composable
fun NotesDetailScreen(
	viewModel: NotesDetailViewModel = hiltViewModel(),
	onBackPressed: () -> Unit,
	onEditClicked: (Int) -> Unit,
	onNavigateToNote: (Int) -> Unit
) {
	val note by viewModel.note.collectAsStateWithLifecycle()
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		floatingActionButton = {
			ExtendedFloatingActionButton(onClick = { onEditClicked(note.id!!) }) {
				Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
				Spacer(modifier = Modifier.width(LocalSpacing.current.text))
				Text(text = stringResource(R.string.action_edit))
			}
		}
	) { paddingValues ->
		Surface(tonalElevation = 8.dp) {
			Column(Modifier.padding(paddingValues)) {
				NavigationAppBar(onBackPressed = onBackPressed)
				NoteId(
					modifier = Modifier.padding(LocalSpacing.current.border),
					text = "# ${note.id}",
					color = MaterialTheme.colorScheme.outline
				)
				if (note.title.isNotBlank()) {
					FormattedNoteContent(
						modifier = Modifier.padding(LocalSpacing.current.border),
						rawText = note.title,
						style = MaterialTheme.typography.titleLarge,
						maxLines = 1
					)
				}

				val uriHandler = LocalUriHandler.current
				val clipboardManager = LocalClipboardManager.current

				FormattedNoteContent(
					modifier = Modifier
						.fillMaxSize()
						.background(MaterialTheme.colorScheme.background)
						.padding(LocalSpacing.current.border),
					rawText = note.content,
					onNavigateToNote = onNavigateToNote,
					onLinkClick = { url -> uriHandler.openUri(url) },
					onLinkLongClick = { url ->
						clipboardManager.setText(AnnotatedString(url))
					}
				)
			}
		}
	}
}