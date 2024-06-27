package com.example.lekhantu.repository

import com.example.lekhantu.db.NoteDatabase
import com.example.lekhantu.model.NoteEntity

class NoteRepository(val db:NoteDatabase){
    suspend fun insertNote(note: NoteEntity) = db.noteDao().insertNote(note)
    suspend fun deleteNote(note: NoteEntity) = db.noteDao().deleteNote(note)
    suspend fun updateNote(note: NoteEntity) = db.noteDao().updateNote(note)
    fun getAllNotes() = db.noteDao().getAllNotes()
    fun searchNotes(query: String?) = db.noteDao().searchNotes(query)


}