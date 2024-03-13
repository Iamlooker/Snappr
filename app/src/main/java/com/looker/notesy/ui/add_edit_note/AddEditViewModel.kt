package com.looker.notesy.ui.add_edit_note

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.notesy.R
import com.looker.notesy.domain.model.InvalidNoteException
import com.looker.notesy.domain.model.Note
import com.looker.notesy.domain.use_case.NoteUseCases
import com.looker.notesy.ui.add_edit_note.navigation.NOTE_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel
@Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val inputNoteId = savedStateHandle.get<Int?>(NOTE_ID_ARG)?.takeIf { it != -1 }

    private var isErrorAlreadyShown = 0

    val isIdEditable = inputNoteId == null

    var noteId by mutableStateOf("")
        private set

    var noteTitle by mutableStateOf("")
        private set

    var noteContent by mutableStateOf("")
        private set

    @get:StringRes
    var errorMessage by mutableStateOf<Int>(-1)
        private set

    val isIdValid = snapshotFlow { noteId }
        .map { currentId ->
            val id = currentId.removePrefix("# ").toIntOrNull()
            !noteUseCases.getNotes.contains(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    init {
        viewModelScope.launch {
            if (inputNoteId == null) {
                val lastNoteId = noteUseCases.getLastId()
                noteId = "# " + (lastNoteId + 1).toString()
                cancel()
                return@launch
            }
            noteUseCases
                .getNote(inputNoteId)
                .collectLatest { note ->
                    noteId = "# ${note.id}"
                    noteTitle = note.title
                    noteContent = note.content
                }
        }
    }

    fun updateNoteId(id: String) {
        noteId = if (!id.startsWith("# ") && id.toIntOrNull() != null) "# $id"
        else id
        resetError()
    }

    fun updateTitle(title: String) {
        noteTitle = title
        resetError()
    }

    fun updateContent(content: String) {
        noteContent = content
        resetError()
    }

    private fun setError(msg: Int) {
        errorMessage = msg
        isErrorAlreadyShown++
    }

    private fun resetError() {
        errorMessage = -1
        isErrorAlreadyShown = 0
    }

    fun saveNote(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val currentId = noteId.removePrefix("# ").toIntOrNull()
                if (!isIdValid.value || currentId == null) throw InvalidNoteException(R.string.label_note_empty)
                noteUseCases.addNote(
                    Note(
                        title = noteTitle,
                        content = noteContent,
                        timeCreated = System.currentTimeMillis(),
                        id = currentId
                    )
                )
                onSuccess()
            } catch (e: InvalidNoteException) {
                setError(e.errorId)
            } finally {
                if (isErrorAlreadyShown > 1) onSuccess()
            }
        }
    }
}