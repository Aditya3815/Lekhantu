package com.example.lekhantu.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lekhantu.MainActivity
import com.example.lekhantu.R
import com.example.lekhantu.databinding.FragmentEditNoteBinding
import com.example.lekhantu.model.NoteEntity
import com.example.lekhantu.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment : Fragment(R.layout.fragment_edit_note) {

    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var currentNote: NoteEntity
    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)
        setInitialCategory()

        binding.editNoteFab.setOnClickListener {
            updateNote(view)
        }

        binding.deleteFab.setOnClickListener {
            deleteNoteDialog(view)
        }
    }

    private fun setInitialCategory() {
        when (currentNote.noteCategory) {
            "Work" -> binding.categoryChipGroup.check(R.id.chipWork)
            "Personal" -> binding.categoryChipGroup.check(R.id.chipPersonal)
            "Ideas" -> binding.categoryChipGroup.check(R.id.chipIdeas)
        }
    }

    private fun getSelectedCategory(): String {
        val selectedChipId = binding.categoryChipGroup.checkedChipId
        return when (selectedChipId) {
            R.id.chipWork -> "Work"
            R.id.chipPersonal -> "Personal"
            R.id.chipIdeas -> "Ideas"
            else -> "Uncategorized" // Default category if somehow no chip is selected
        }
    }

    private fun updateNote(view: View) {
        val title = binding.editNoteTitle.text.toString().trim()
        val desc = binding.editNoteDesc.text.toString().trim()
        val category = getSelectedCategory()
        val currentDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (title.isNotEmpty()) {
            val note = NoteEntity(currentNote.id, title, desc, category, currentDate)
            noteViewModel.updateNote(note)
            Snackbar.make(view, "Note Updated", Snackbar.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Snackbar.make(view, "Title Required", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun deleteNoteDialog(view: View) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_note, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.dialogTitle).text = "Delete Note"
        dialogView.findViewById<TextView>(R.id.dialogMessage).text =
            "Are you sure you want to delete this note?"

        dialogView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            noteViewModel.deleteNote(currentNote)
            Snackbar.make(view, "Note Deleted", Snackbar.LENGTH_SHORT).show()
            alertDialog.dismiss()
            view.findNavController().navigate(R.id.action_editNoteFragment_to_homeFragment)
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editNoteBinding = null
    }
}