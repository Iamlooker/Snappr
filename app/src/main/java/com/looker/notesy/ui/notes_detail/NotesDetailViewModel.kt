package com.looker.notesy.ui.notes_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.use_case.NoteUseCases
import com.looker.notesy.ui.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesDetailViewModel @Inject constructor(
    useCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val noteId: Int = savedStateHandle["noteId"]!!

    val note = useCases.getNote(noteId)
        .asStateFlow(initial = Note("", "", 0L, -1))
}
