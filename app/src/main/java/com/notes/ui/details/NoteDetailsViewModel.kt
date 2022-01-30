package com.notes.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.ui.list.NoteListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class NoteDetailsViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {

    private val _note = MutableLiveData<NoteListItem>()
    val note: LiveData<NoteListItem> = _note

    fun getNoteDetails(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _note.postValue(
                noteDatabase.noteDao().findNoteById(noteId)
            )
        }
    }

}