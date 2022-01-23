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

    private val _navigateToNoteCreation = MutableLiveData<Unit?>()
    val navigateToNoteCreation: LiveData<Unit?> = _navigateToNoteCreation

    init {
        getAllNotes()
    }

    fun onCreateNoteClick() {
        _navigateToNoteCreation.postValue(Unit)
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

    fun insertNote(noteDbo: NoteDbo) {
        noteDatabase.noteDao().insertAll(noteDbo)
        getAllNotes()
    }

    fun deleteNote(id: Long) {
        noteDatabase.noteDao().deleteNote(id)
        getAllNotes()
    }

    fun editNote(noteDbo: NoteDbo) {
        noteDatabase.noteDao().updateEmployee(noteDbo)
        getAllNotes()
    }
}

data class NoteListItem(
    val id: Long,
    var title: String,
    var content: String
)