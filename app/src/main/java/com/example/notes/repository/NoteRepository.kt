package com.example.notes.repository

import com.example.notes.database.NoteDataBase
import com.example.notes.model.Note

class NoteRepository(db: NoteDataBase) {

    private val dao = db.getNoteDao()

    suspend fun insertNote(note: Note) = dao.insertNote(note)
    suspend fun deleteNote(note: Note) = dao.deleteNote(note)
    suspend fun updateNote(note: Note) = dao.updateNote(note)

    fun getAllNotes() = dao.getAllNotes()
    fun searchNote(query: String?) = dao.searchNote(query)
}