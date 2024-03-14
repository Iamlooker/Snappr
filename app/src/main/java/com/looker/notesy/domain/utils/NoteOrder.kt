package com.looker.notesy.domain.utils

sealed class NoteOrder(val orderType: OrderType) {

    data class Title(val order: OrderType) : NoteOrder(order)

    data class Date(val order: OrderType) : NoteOrder(order)

    data class Id(val order: OrderType) : NoteOrder(order)

    infix fun by(orderType: OrderType): NoteOrder {
        return when (this) {
            is Date -> Date(orderType)
            is Title -> Title(orderType)
            is Id -> Id(orderType)
        }
    }
}
