package com.example.lekhantu.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.lekhantu.MainActivity
import com.example.lekhantu.R
import com.example.lekhantu.databinding.FragmentAddNoteBinding
import com.example.lekhantu.model.NoteEntity
import com.example.lekhantu.viewmodel.NoteViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNoteView = view
        noteViewModel = (activity as MainActivity).noteViewModel
        binding.addNoteFab.setOnClickListener {
            saveNote(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }

    private fun saveNote(view: View) {
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()
        val noteCategory = getSelectedCategory()
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (noteTitle.isNotEmpty()) {
            if (noteCategory != null) {
                val note = NoteEntity(0, noteTitle, noteDesc, noteCategory, currentDate)
                noteViewModel.addNote(note)
                Snackbar.make(addNoteView, "Note Saved", Snackbar.LENGTH_SHORT).show()
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Snackbar.make(addNoteView, "Please select a category", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(addNoteView, "Title Required", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getSelectedCategory(): String? {
        val chipGroup = binding.categoryChipGroup
        val checkedChipId = chipGroup.checkedChipId
        return if (checkedChipId != View.NO_ID) {
            val selectedChip = chipGroup.findViewById<Chip>(checkedChipId)
            selectedChip.text.toString()
        } else {
            null
        }
    }
}