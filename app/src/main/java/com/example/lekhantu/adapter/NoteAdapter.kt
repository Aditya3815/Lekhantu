package com.example.lekhantu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lekhantu.databinding.NoteLayoutBinding
import com.example.lekhantu.fragments.HomeFragmentDirections
import com.example.lekhantu.model.NoteEntity

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(val itemBinding: NoteLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.id == newItem.id
            oldItem.noteTitle == newItem.noteTitle
            oldItem.noteDesc == newItem.noteDesc
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
      return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.itemBinding.apply {
            noteTitle.text = currentNote.noteTitle
            noteDate.text = currentNote.date
            noteDesc.text = currentNote.noteDesc
            noteCategory.text = currentNote.noteCategory
            root.setOnClickListener {
                val direction =
                    HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)
                it.findNavController().navigate(direction)
            }
        }

    }
}