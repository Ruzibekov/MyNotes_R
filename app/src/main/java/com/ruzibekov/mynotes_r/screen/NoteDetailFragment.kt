package com.ruzibekov.mynotes_r.screen

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ruzibekov.mynotes_r.R
import com.ruzibekov.mynotes_r.base.BaseFragment
import com.ruzibekov.mynotes_r.databinding.FragmentNoteDetailBinding
import com.ruzibekov.mynotes_r.room.NoteDao
import com.ruzibekov.mynotes_r.room.NoteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteDetailFragment : BaseFragment(R.layout.fragment_note_detail) {
    companion object {
        const val KEY_NOTE_TITLE = "key-note-title"
        const val KEY_NOTE_TEXT = "key-note-text"
        const val KEY_NOTE_ID = "key-note-id"
    }

    private lateinit var binding: FragmentNoteDetailBinding
    private var noteID = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteDetailBinding.bind(view)
        getNoteDatas()

        binding.etNoteDetailText.setShadowLayer(
            binding.etNoteDetailText.extendedPaddingBottom.toFloat(),
            0f,
            0f,
            Color.TRANSPARENT)

        binding.imageButtonNoteDetailBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageButtonNoteDetailCheck.setOnClickListener {
            if (binding.etNoteDetailTitle.text.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (noteID == -1) noteDao.insertNote(NoteModel(
                        0,
                        binding.etNoteDetailTitle.text.toString(),
                        binding.etNoteDetailText.text.toString()))
                    else noteDao.updateNote(
                        binding.etNoteDetailTitle.text.toString(),
                        binding.etNoteDetailText.text.toString(),
                        noteID)
                }
                findNavController().popBackStack()
            }else{
                Toast.makeText(context, getText(R.string.title_is_empty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getNoteDatas() {
        if (arguments?.getString(KEY_NOTE_TITLE) != null) {
            noteID = arguments?.getInt(KEY_NOTE_ID) ?: -1
            binding.etNoteDetailTitle.setText(arguments?.getString(KEY_NOTE_TITLE))
            binding.etNoteDetailText.setText(arguments?.getString(KEY_NOTE_TEXT))
        }
    }

}