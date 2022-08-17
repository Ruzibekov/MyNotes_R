package com.ruzibekov.mynotes_r.room

import android.content.Context
import androidx.room.Room

class NoteRepository(private val context: Context) {
    private var noteDao: NoteDao? = null

    fun getNoteDao(): NoteDao {
        if (noteDao == null)
            noteDao = Room.databaseBuilder(context,
                NoteDatabase::class.java, "note-database").build().noteDao()

        return noteDao!!
    }
}