package com.ruzibekov.mynotes_r.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ruzibekov.mynotes_r.R
import com.ruzibekov.mynotes_r.databinding.ItemNoteBinding
import com.ruzibekov.mynotes_r.room.NoteModel

class NoteListAdapter(private val fragment: MainFragment) :
    ListAdapter<NoteModel, NoteListAdapter.ViewHolder>(ItemComparator()) {

    class ViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(noteModel: NoteModel) {
            binding.tvItemNoteTitle.text = noteModel.title
            binding.tvItemNoteText.text = noteModel.text
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.rootView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(NoteDetailFragment.KEY_NOTE_TITLE, getItem(position).title)
            bundle.putString(NoteDetailFragment.KEY_NOTE_TEXT, getItem(position).text)
            bundle.putInt(NoteDetailFragment.KEY_NOTE_ID, getItem(position).id)
            holder.itemView.findNavController()
                .navigate(R.id.action_mainFragment_to_noteDetailFragment, bundle)
        }

        holder.itemView.findViewById<ImageView>(R.id.iv_item_note_delete).setOnClickListener {
            fragment.deleteNote(currentList[holder.adapterPosition])
            val filterList = currentList.toMutableList()
            filterList.removeAt(holder.adapterPosition)
            submitList(filterList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun submitList(list: List<NoteModel>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }

    fun filterItems(searchText: String, firstNoteList: List<NoteModel>) {
        val filteredList = mutableListOf<NoteModel>()
        for (i in firstNoteList) {
            if (i.title.lowercase().contains(searchText.lowercase()))
                filteredList.add(i)
        }
        submitList(filteredList)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<NoteModel>,
        currentList: MutableList<NoteModel>,
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }
}