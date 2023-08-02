package com.looker.notesy.domain.model

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.notesy.ui.theme.*

@Entity
@Stable
data class Note(
	val title: String,
	val content: String,
	val timeCreated: Long,
	val color: Int,
	@PrimaryKey val id: Int? = null
)

class InvalidNoteException(message: String?): Exception(message)