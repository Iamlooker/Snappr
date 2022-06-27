package com.looker.notesy.core

import com.looker.notesy.feature_note.domain.model.Note

sealed class UiEvents {
	object EMPTY : UiEvents()
	data class Restored(val note: Note?) : UiEvents()
	data class ShowSnackBar(val message: String = "") : UiEvents()
	data class DeleteConfirmation(val note: Note? = null, val show: Boolean = false) : UiEvents()
	object SaveNote : UiEvents()
}