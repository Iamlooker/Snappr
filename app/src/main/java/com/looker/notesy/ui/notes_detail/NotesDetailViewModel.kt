package com.looker.notesy.ui.notes_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.repository.NoteRepository
import com.looker.notesy.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotesDetailViewModel @Inject constructor(
	private val useCases: NoteUseCases,
	private val repository: NoteRepository,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val noteId: Int = savedStateHandle["noteId"]!!

	val note = useCases.getNote(noteId)
		.filterNotNull()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = Note("", "", 0L, -1)
		)
}