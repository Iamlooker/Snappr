package com.looker.notesy.domain.use_case

import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository

class DeleteNote(private val repository: NoteRepository) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}