package com.ruzibekov.mynotes_r.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ruzibekov.mynotes_r.R
import com.ruzibekov.mynotes_r.room.NoteDao
import com.ruzibekov.mynotes_r.room.NoteRepository

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    private var noteDataRepository: NoteRepository? = null
    lateinit var noteDao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteDao = getNoteDataRepo().getNoteDao()
    }

    fun getNoteDataRepo(): NoteRepository{
        if(noteDataRepository == null)
            noteDataRepository = NoteRepository(activity?.applicationContext!!)
        return  noteDataRepository!!
    }

    fun navigateTo(navigationId: Int){
        findNavController().navigate(navigationId)
    }

    fun navigateTo(navigationId: Int, bundle: Bundle){
        findNavController().navigate(navigationId, bundle)
    }
}