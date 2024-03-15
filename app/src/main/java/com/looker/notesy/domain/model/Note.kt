package com.looker.notesy.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Stable
data class Note(
    val title: String,
    val content: String,
    @ColumnInfo("time")
    val timeCreated: Long,
    @PrimaryKey
    val id: Int? = null,
) {
    val contentType: NoteContentType
        get() = if (title.isBlank()) NoteContentType.NoTitle
        else if (content.isBlank()) NoteContentType.NoContent
        else NoteContentType.Full
}

enum class NoteContentType {
    NoTitle, NoContent, Full
}

class InvalidNoteException(@StringRes val errorId: Int, message: String? = null) :
    Exception(message)
