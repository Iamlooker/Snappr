package com.looker.notesy.core

sealed class UiEvents {
	data class ShowSnackBar(val message: String = "", val show: Boolean = false) : UiEvents()
	data class DeleteConfirmation(val show: Boolean = false): UiEvents()
	object SaveNote: UiEvents()
}