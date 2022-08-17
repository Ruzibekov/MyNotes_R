package com.ruzibekov.mynotes_r.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.ruzibekov.mynotes_r.R
import com.ruzibekov.mynotes_r.base.BaseFragment
import com.ruzibekov.mynotes_r.databinding.FragmentMainBinding
import com.ruzibekov.mynotes_r.room.NoteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : BaseFragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    lateinit var itemNotFoundImage: ImageView
    private var searchItemList = mutableListOf<NoteModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        itemNotFoundImage = binding.ivMainNotesNotFound
        binding.btnMainAddNewNote.setOnClickListener {
            navigateTo(R.id.action_mainFragment_to_noteDetailFragment)
        }
        loadAllNotes()
        onSearch()
    }

    private fun onSearch() {
        CoroutineScope(Dispatchers.IO).launch {
            binding.etMainSearch.addTextChangedListener { text ->
                (binding.rvMainNotes.adapter as NoteListAdapter).filterItems(text.toString(),
                    searchItemList)
            }
        }

    }

    private fun loadAllNotes() {
        Log.i("mylog", "Restart")
        val adapter = NoteListAdapter(this)
        binding.rvMainNotes.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            val noteList = getNoteDataRepo().getNoteDao().getAllNotes()
            searchItemList = noteList.toMutableList()
            CoroutineScope(Dispatchers.Main).launch {
                adapter.submitList(noteList)
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                super.onChanged()
                checkNotesListCount()
                CoroutineScope(Dispatchers.IO).launch {
                    searchItemList = getNoteDataRepo().getNoteDao().getAllNotes().toMutableList()
                }
            }
        })
    }

    private fun checkNotesListCount() {
        binding.ivMainNotesNotFound.visibility =
            if ((binding.rvMainNotes.adapter?.itemCount ?: 0) <= 1) View.VISIBLE
            else View.INVISIBLE
    }

    fun deleteNote(noteModel: NoteModel) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.delete(noteModel)
        }
    }
}