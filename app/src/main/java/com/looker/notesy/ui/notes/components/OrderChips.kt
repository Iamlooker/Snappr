package com.looker.notesy.ui.notes.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
    noteOrder: NoteOrder,
    onOrderChange: (NoteOrder) -> Unit,
    modifier: Modifier = Modifier,
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
            isSelected = { noteOrder is NoteOrder.Title },
            onClick = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) }
        )
        OrderChip(
            text = stringResource(R.string.label_order_date),
            isSelected = { noteOrder is NoteOrder.Date },
            onClick = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) }
        )
        OrderChip(
            text = stringResource(R.string.label_order_id),
            isSelected = { noteOrder is NoteOrder.Id },
            onClick = { onOrderChange(NoteOrder.Id(noteOrder.orderType)) }
        )
        Spacer(
            modifier = Modifier
                .height(FilterChipDefaults.Height - 4.dp)
                .width(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
        OrderChip(
            text = stringResource(R.string.label_order_ascending),
            isSelected = { noteOrder.orderType == OrderType.Ascending },
            icon = Icons.Rounded.ArrowUpward,
            onClick = { onOrderChange(noteOrder by OrderType.Ascending) }
        )
        OrderChip(
            text = stringResource(R.string.label_order_descending),
            isSelected = { noteOrder.orderType == OrderType.Descending },
            icon = Icons.Rounded.ArrowDownward,
            onClick = { onOrderChange(noteOrder by OrderType.Descending) }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun OrderChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isSelected: () -> Boolean = { false },
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
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                    (slideOutVertically { height -> -height } + fadeOut())
                        } else {
                            (slideInVertically { height -> -height } + fadeIn()) togetherWith
                                    (slideOutVertically { height -> height } + fadeOut())
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
private fun OrderChipPreview() {
    NotesyTheme {
        Surface {
            OrderChips(noteOrder = NoteOrder.Title(OrderType.Ascending), onOrderChange = {})
        }
    }
}
