package com.example.lekhantu.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lekhantu.model.NoteEntity
import com.example.lekhantu.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(app: Application, private val noteRepository: NoteRepository) : ViewModel() {
    fun addNote(note: NoteEntity) = viewModelScope.launch {
        noteRepository.insertNote(note)
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        noteRepository.deleteNote(note)
    }

    fun updateNote(note: NoteEntity) = viewModelScope.launch {
        noteRepository.updateNote(note)
    }

    fun allNotes() = noteRepository.getAllNotes()
    fun searchNotes(searchQuery: String?) = noteRepository.searchNotes(searchQuery)

}