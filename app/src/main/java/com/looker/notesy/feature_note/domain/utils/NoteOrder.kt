package com.looker.notesy.feature_note.domain.utils

sealed class NoteOrder(val orderType: OrderType) {
	class Title(orderType: OrderType): NoteOrder(orderType)
	class Date(orderType: OrderType): NoteOrder(orderType)

	fun copy(orderType: OrderType) : NoteOrder {
		return when(this) {
			is Date -> Date(orderType)
			is Title -> Title(orderType)
		}
	}
}
