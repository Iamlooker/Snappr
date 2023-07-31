package com.looker.notesy.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesyTopAppBar(
	title: String,
	modifier: Modifier = Modifier,
	scrollBehavior: TopAppBarScrollBehavior? = null
) {
	CenterAlignedTopAppBar(
		title = {
			Text(
				text = title,
				style = MaterialTheme.typography.headlineMedium
			)
		},
		scrollBehavior = scrollBehavior,
		modifier = modifier
	)
}