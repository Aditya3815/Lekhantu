package com.example.lekhantu.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.lekhantu.MainActivity
import com.example.lekhantu.R
import com.example.lekhantu.adapter.NoteAdapter
import com.example.lekhantu.databinding.FragmentHomeBinding
import com.example.lekhantu.model.NoteEntity
import com.example.lekhantu.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        setupRecyclerView()
        setupSearchView()

        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter()
        binding.homeRecyclerView.apply {
            adapter = noteAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
        }
        setupSwipeToDelete()
        activity?.let {
            noteViewModel.allNotes().observe(viewLifecycleOwner) { notes ->
                noteAdapter.differ.submitList(notes)
                updateUI(notes)
            }
        }
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = noteAdapter.differ.currentList[position]
                noteViewModel.deleteNote(note)
                Snackbar.make(binding.root, "Note deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        noteViewModel.addNote(note)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.homeRecyclerView)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchNote(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(300) // Debounce for 300ms
                    newText?.let { searchNote(it) }
                }
                return true
            }
        })
    }

    private fun searchNote(query: String) {
        val searchQuery = "%$query%"
        noteViewModel.searchNotes(searchQuery).observe(viewLifecycleOwner) { notes ->
            noteAdapter.differ.submitList(notes)
            updateUI(notes)
            if (notes.isEmpty() && query.isNotEmpty()) {
                showSnackbar("No results found for '$query'")
            }
        }
    }

    private fun updateUI(notes: List<NoteEntity>) {
        if (notes.isNotEmpty()) {
            binding.homeRecyclerView.visibility = View.VISIBLE
            binding.emptyNotesImage.visibility = View.GONE
            binding.searchView.visibility = View.VISIBLE
            binding.searchCardView.visibility = View.VISIBLE
        } else {
            binding.homeRecyclerView.visibility = View.GONE
            binding.emptyNotesImage.visibility = View.VISIBLE
            binding.searchView.visibility = View.GONE
            binding.searchCardView.visibility = View.GONE
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
        searchJob?.cancel()
    }
}