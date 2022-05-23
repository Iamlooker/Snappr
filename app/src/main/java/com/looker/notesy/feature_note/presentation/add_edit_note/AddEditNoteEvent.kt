package com.looker.notesy.feature_note.presentation.add_edit_note

sealed class AddEditNoteEvent {
	data class EnteredTitle(val value: String) : AddEditNoteEvent()
	data class EnteredContent(val value: String) : AddEditNoteEvent()
	object SaveNote : AddEditNoteEvent()
}