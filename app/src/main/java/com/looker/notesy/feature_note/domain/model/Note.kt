package com.looker.notesy.feature_note.domain.model

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
) {
	companion object {
		val noteColors = listOf(
			RedOrange,
			RedPink,
			BabyBlue,
			Violet,
			LightGreen
		)
	}
}


class InvalidNoteException(message: String): Exception(message)