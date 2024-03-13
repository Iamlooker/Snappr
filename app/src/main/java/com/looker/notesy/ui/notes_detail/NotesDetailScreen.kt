package com.looker.notesy.ui.notes_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalFoundationApi::class)
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
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.verticalScroll(rememberScrollState())
				.combinedClickable(
					onDoubleClick = { onEditClicked(note.id!!) },
					indication = null,
					interactionSource = remember { MutableInteractionSource() },
					onClick = {}
				)
		) {
			NavigationAppBar(
				onBackPressed = onBackPressed,
				title = {
					Text(
						text = stringResource(R.string.label_view_mode),
						color = MaterialTheme.colorScheme.outline
					)
				}
			)
			NoteId(
				modifier = Modifier.padding(LocalSpacing.current.border),
				text = "# ${note.id}",
				color = MaterialTheme.colorScheme.outline
			)
			if (note.title.isNotBlank()) {
				FormattedNoteContent(
					modifier = Modifier.padding(LocalSpacing.current.border),
					rawText = note.title,
					style = MaterialTheme.typography.headlineMedium,
				)
			}

			HorizontalDivider()

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
			Spacer(
				modifier = Modifier
					.fillMaxWidth()
					.height(72.dp)
					.background(MaterialTheme.colorScheme.background)
			)
		}
	}
}