package com.looker.notesy.ui.add_edit_note.components

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.notesy.ui.theme.NotesyTheme
import com.looker.notesy.ui.theme.top

@Composable
fun TransparentTextField(
	text: String,
	hint: String,
	modifier: Modifier = Modifier,
	onValueChange: (String) -> Unit,
	singleLine: Boolean = false,
	shape: CornerBasedShape = RoundedCornerShape(0.dp),
	containerColor: Color = MaterialTheme.colorScheme.surface,
	contentColor: Color = MaterialTheme.colorScheme.onBackground,
	textStyle: TextStyle = LocalTextStyle.current,
	isError: Boolean = false
) {
	val textFieldColor = TextFieldDefaults.colors(
		focusedContainerColor = containerColor,
		unfocusedContainerColor = containerColor,
		focusedTextColor = contentColor,
		unfocusedTextColor = contentColor,
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
		shape = shape,
		textStyle = textStyle,
		isError = isError
	)
}

@Preview
@Composable
fun TextPreview() {
	NotesyTheme {
		TransparentTextField(text = "", hint = "LOL", onValueChange = {})
	}
}