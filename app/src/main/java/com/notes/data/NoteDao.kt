package com.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<NoteDbo>

    @Insert
    fun insertAll(vararg notes: NoteDbo)

    @Query("DELETE FROM notes WHERE id=:noteId")
    fun deleteNote(noteId: Long?)

    @Update
    fun updateEmployee(note: NoteDbo)

}