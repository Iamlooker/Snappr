package com.looker.notesy.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.notesy.feature_note.domain.utils.NoteOrder
import com.looker.notesy.feature_note.domain.utils.OrderType
import com.looker.notesy.ui.theme.NotesyTheme

@Composable
fun OrderChips(
	modifier: Modifier = Modifier,
	noteOrder: NoteOrder,
	onOrderChange: (NoteOrder) -> Unit
) {
	Row(modifier = modifier.padding(horizontal = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
		OrderChip(text = "Title", isSelected = noteOrder is NoteOrder.Title) {
			onOrderChange(NoteOrder.Title(noteOrder.orderType))
		}
		OrderChip(text = "Date", isSelected = noteOrder is NoteOrder.Date) {
			onOrderChange(NoteOrder.Date(noteOrder.orderType))
		}
		Spacer(modifier = Modifier.width(Dp.Hairline))
		OrderChip(
			text = "Ascending",
			isSelected = noteOrder.orderType is OrderType.Ascending,
			icon = {
				Icon(imageVector = Icons.Default.FilterList, contentDescription = null)
			}
		) {
			onOrderChange(noteOrder.copy(OrderType.Ascending))
		}
		OrderChip(
			text = "Descending",
			isSelected = noteOrder.orderType is OrderType.Descending,
			icon = {
				Icon(
					modifier = Modifier.rotate(180f),
					imageVector = Icons.Default.FilterList,
					contentDescription = null
				)
			}
		) {
			onOrderChange(noteOrder.copy(OrderType.Descending))
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderChip(
	text: String,
	modifier: Modifier = Modifier,
	isSelected: Boolean = false,
	icon: (@Composable () -> Unit)? = null,
	onClick: () -> Unit
) {
	FilterChip(
		modifier = modifier,
		selected = isSelected,
		onClick = onClick,
		label = {
			Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis)
		},
		leadingIcon = { icon?.let { icon() } },
		selectedIcon = { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
	)
}

@Preview
@Composable
fun OrderChipPreview() {
	NotesyTheme {
		Surface {
			OrderChips(noteOrder = NoteOrder.Title(OrderType.Ascending), onOrderChange = {})
		}
	}
}