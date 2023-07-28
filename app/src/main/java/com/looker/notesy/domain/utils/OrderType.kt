package com.looker.notesy.domain.utils

sealed class OrderType {
	object Ascending: OrderType()
	object Descending: OrderType()
}
