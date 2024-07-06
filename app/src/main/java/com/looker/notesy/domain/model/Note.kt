package com.looker.notesy.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun formatDate(
    time: Long,
    dateTimeFormat: DateTimeFormat = DateTimeFormat.Short,
    locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
): String {
    val format = SimpleDateFormat(dateTimeFormat.format, locale)
    return format.format(Date(time))
}

enum class DateTimeFormat(val format: String) {
    Short("dd-MMM-yy"), Long("EEEE, dd-MMM-yyyy"),
}
