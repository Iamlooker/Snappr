package com.looker.notesy.ui.notes.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.notesy.R
import com.looker.notesy.domain.utils.NoteOrder
import com.looker.notesy.domain.utils.OrderType
import com.looker.notesy.ui.theme.NotesyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderChips(
	modifier: Modifier = Modifier,
	noteOrder: NoteOrder,
	onOrderChange: (NoteOrder) -> Unit
) {
	Row(
		modifier = modifier
			.horizontalScroll(state = rememberScrollState())
			.padding(horizontal = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		OrderChip(
			text = stringResource(R.string.label_order_title),
			isSelected = { noteOrder is NoteOrder.Title }) {
			onOrderChange(NoteOrder.Title(noteOrder.orderType))
		}
		OrderChip(
			text = stringResource(R.string.label_order_date),
			isSelected = { noteOrder is NoteOrder.Date }) {
			onOrderChange(NoteOrder.Date(noteOrder.orderType))
		}
		OrderChip(
			text = stringResource(R.string.label_order_id),
			isSelected = { noteOrder is NoteOrder.Id }) {
			onOrderChange(NoteOrder.Id(noteOrder.orderType))
		}
		Spacer(
			modifier = Modifier
				.height(FilterChipDefaults.Height - 4.dp)
				.width(1.dp)
				.background(MaterialTheme.colorScheme.outline)
		)
		OrderChip(
			text = stringResource(R.string.label_order_ascending),
			isSelected = { noteOrder.orderType == OrderType.Ascending },
			icon = Icons.Default.FilterList
		) {
			onOrderChange(noteOrder.setOrder(OrderType.Ascending))
		}
		OrderChip(
			text = stringResource(R.string.label_order_descending),
			isSelected = { noteOrder.orderType == OrderType.Descending },
			icon = Icons.Default.Sort
		) {
			onOrderChange(noteOrder.setOrder(OrderType.Descending))
		}
	}
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterial3Api
@Composable
fun OrderChip(
	text: String,
	modifier: Modifier = Modifier,
	isSelected: () -> Boolean = { false },
	icon: ImageVector? = null,
	onClick: () -> Unit
) {
	FilterChip(
		modifier = modifier,
		selected = isSelected(),
		onClick = onClick,
		label = {
			Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis)
		},
		leadingIcon = {
			icon?.let { leadingIcon ->
				AnimatedContent(
					targetState = if (isSelected()) Icons.Default.Done else leadingIcon,
					transitionSpec = {
						if (targetState == Icons.Default.Done) {
							slideInVertically { height -> height } + fadeIn() with
									slideOutVertically { height -> -height } + fadeOut()
						} else {
							slideInVertically { height -> -height } + fadeIn() with
									slideOutVertically { height -> height } + fadeOut()
						} using SizeTransform(false)
					},
					label = ""
				) {
					Icon(imageVector = it, contentDescription = null)
				}
			}
		}
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