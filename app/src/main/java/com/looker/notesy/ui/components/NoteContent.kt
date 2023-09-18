package com.looker.notesy.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.looker.notesy.ui.utils.SymbolAnnotationType
import com.looker.notesy.ui.utils.noteFormatter

@Composable
fun FormattedNoteContent(
	rawText: String,
	modifier: Modifier = Modifier,
	enableSelection: Boolean = true,
	style: TextStyle = LocalTextStyle.current,
	fontWeight: FontWeight? = null,
	color: Color = LocalContentColor.current,
	maxLines: Int = Int.MAX_VALUE,
	overflow: TextOverflow = TextOverflow.Ellipsis,
	textDecoration: TextDecoration? = null,
	onNavigateToNote: (Int) -> Unit = {},
	onLinkClick: (String) -> Unit = {},
	onLinkLongClick: (String) -> Unit = {}
) {
	val styleText = noteFormatter(text = rawText)

	if (enableSelection) {
		SelectionContainer {
			Text(
				text = styleText,
				modifier = modifier,
				color = color,
				style = style,
				fontWeight = fontWeight,
				maxLines = maxLines,
				overflow = overflow,
				textDecoration = textDecoration,
				inlineContent = noteInlineContent(onNavigateToNote, onLinkClick, onLinkLongClick)
			)
		}
	} else {
		Text(
			text = styleText,
			modifier = modifier,
			color = color,
			style = style,
			fontWeight = fontWeight,
			maxLines = maxLines,
			overflow = overflow,
			textDecoration = textDecoration,
			inlineContent = noteInlineContent(onNavigateToNote, onLinkClick, onLinkLongClick)
		)
	}

}

private fun noteInlineContent(
	onNavigateToNote: (Int) -> Unit,
	onLinkClick: (String) -> Unit,
	onLinkLongClick: (String) -> Unit
) = mapOf(
	SymbolAnnotationType.NOTE.name to InlineTextContent(
		placeholder = Placeholder(
			width = 86.sp,
			height = 32.sp,
			placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
		)
	) {
		DisableSelection {
			Surface(
				tonalElevation = 2.dp,
				shape = MaterialTheme.shapes.extraSmall,
				onClick = { onNavigateToNote(it.toInt()) }
			) {
				NoteId(
					modifier = Modifier.padding(
						vertical = 4.dp,
						horizontal = 12.dp
					),
					text = "# $it",
					style = MaterialTheme.typography.labelMedium
				)
			}
		}
	},
	SymbolAnnotationType.LINK.name to InlineTextContent(
		placeholder = Placeholder(
			width = 200.sp,
			height = 32.sp,
			placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
		)
	) {
		DisableSelection {
			HyperLink(
				link = it,
				onClick = { onLinkClick(it) },
				onLongClick = { onLinkLongClick(it) }
			)
		}
	}
)