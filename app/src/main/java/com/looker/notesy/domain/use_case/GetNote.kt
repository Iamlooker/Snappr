package com.looker.notesy.domain.use_case

import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class GetNote(private val repository: NoteRepository) {
    operator fun invoke(id: Int): Flow<Note> {
        return repository.getNoteById(id).filterNotNull()
    }
}