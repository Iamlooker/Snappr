package com.looker.notesy.domain.use_case

import com.looker.notesy.R
import com.looker.notesy.domain.model.InvalidNoteException
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository

class AddNote(private val repository: NoteRepository) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.content.isBlank() && note.title.isBlank()) throw InvalidNoteException(R.string.label_note_empty)
        repository.insertNote(note)
    }
}