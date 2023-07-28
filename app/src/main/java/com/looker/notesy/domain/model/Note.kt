package com.looker.notesy.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.notesy.ui.theme.*

@Entity
data class Note(
	val title: String,
	val content: String,
	val timeCreated: Long,
	val color: Int,
	@PrimaryKey val id: Int? = null
)

class InvalidNoteException(message: String): Exception(message)