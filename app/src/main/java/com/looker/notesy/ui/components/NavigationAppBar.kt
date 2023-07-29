package com.looker.notesy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.looker.notesy.R
import com.looker.notesy.ui.utils.LocalSpacing

@Composable
fun NavigationAppBar(
	onBackPressed: () -> Unit,
	modifier: Modifier = Modifier,
	title: (@Composable () -> Unit)? = null,
	navigationIcon: ImageVector = Icons.Rounded.ArrowBack,
	iconDescription: String? = stringResource(id = R.string.action_go_back)
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		IconButton(
			onClick = onBackPressed
		) {
			Icon(imageVector = navigationIcon, contentDescription = iconDescription)
		}
		ProvideTextStyle(value = MaterialTheme.typography.headlineSmall) {
			title?.let { it() }
		}
	}
}