package com.looker.notesy.feature_note.presentation.add_edit_note.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.looker.notesy.ui.theme.NotesyTheme

@Composable
fun TransparentTextField(
	text: String,
	hint: String,
	modifier: Modifier = Modifier,
	onValueChange: (String) -> Unit,
	singleLine: Boolean = false,
	containerColor: Color = MaterialTheme.colorScheme.surface,
	contentColor: Color = MaterialTheme.colorScheme.onBackground,
	textStyle: TextStyle = LocalTextStyle.current
) {
	val textFieldColor = TextFieldDefaults.textFieldColors(
		containerColor = containerColor,
		textColor = contentColor,
		focusedIndicatorColor = Color.Transparent,
		unfocusedIndicatorColor = Color.Transparent
	)
	TextField(
		modifier = modifier,
		value = text,
		placeholder = {
			Text(text = hint, style = textStyle)
		},
		onValueChange = onValueChange,
		singleLine = singleLine,
		colors = textFieldColor,
		shape = Shapes.None,
		textStyle = textStyle
	)
}

@Preview
@Composable
fun TextPreview() {
	NotesyTheme {
		TransparentTextField(text = "", hint = "LOL", onValueChange = {})
	}
}