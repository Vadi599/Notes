package com.notes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteListViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {

    private val _notes = MutableLiveData<List<NoteListItem>?>()
    val notes: LiveData<List<NoteListItem>?> = _notes

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.postValue(
                noteDatabase.noteDao().getAll().map {
                    NoteListItem(
                        id = it.id,
                        title = it.title,
                        content = it.content,
                    )
                }
            )
        }
    }

    fun insertNote(note: NoteDbo) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().insertAll(note)
            getAllNotes()
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().deleteNote(id)
            getAllNotes()
        }
    }

    fun editNote(note: NoteListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().updateNote(note)
            getAllNotes()
        }
    }
}

data class NoteListItem(
    val id: Long,
    var title: String,
    var content: String
)