package com.looker.notesy.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Stable
data class Note(
	val title: String,
	val content: String,
	val timeCreated: Long,
	val color: Int,
	@PrimaryKey val id: Int? = null
)

class InvalidNoteException(@StringRes val errorId: Int, message: String? = null): Exception(message)