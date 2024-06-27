package com.example.lekhantu.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lekhantu.model.NoteEntity

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE noteTitle LIKE :searchQuery OR noteDesc LIKE :searchQuery")
    fun searchNotes(searchQuery: String?): LiveData<List<NoteEntity>>


}