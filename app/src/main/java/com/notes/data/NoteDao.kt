package com.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.notes.ui.list.NoteListItem

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<NoteDbo>

    @Insert
    fun insertAll(vararg notes: NoteDbo)

    @Query("DELETE FROM notes WHERE id=:noteId")
    fun deleteNote(noteId: Long)

    @Update(entity = NoteDbo::class)
    fun updateNote(note: NoteListItem)

    @Query("SELECT * FROM notes WHERE id=:noteId")
    fun findNoteById(noteId: Long): NoteListItem

}