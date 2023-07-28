package com.looker.notesy.domain.utils

sealed class NoteOrder(val orderType: OrderType) {

	data class Title(val order: OrderType) : NoteOrder(order)

	data class Date(val order: OrderType) : NoteOrder(order)

	fun setOrder(orderType: OrderType) : NoteOrder {
		return when(this) {
			is Date -> Date(orderType)
			is Title -> Title(orderType)
		}
	}
}
