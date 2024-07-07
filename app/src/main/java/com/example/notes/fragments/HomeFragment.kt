package com.example.notes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.MainActivity
import com.example.notes.R
import com.example.notes.adapter.NoteAdapter
import com.example.notes.databinding.FragmentHomeBinding
import com.example.notes.model.Note
import com.example.notes.viewmodel.NoteViewModel

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener{

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel

        setUpRecyclerView()

        binding.addButton.setOnClickListener {
            it.findNavController().navigate(
                R.id.action_homeFragment_to_newNoteFragment
            )
        }
    }

    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()

        binding.notesRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let {
            noteViewModel.getAllNotes().observe(
                viewLifecycleOwner
            ) { note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }

    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if(note.isNotEmpty()){
                binding.noNotesCardView.visibility = View.GONE
                binding.notesRecyclerView.visibility = View.VISIBLE
            }else{
                binding.noNotesCardView.visibility = View.VISIBLE
                binding.notesRecyclerView.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Finish functionality")
        //searchNote(query)
        return false
    }

    private fun searchNote(query: String?) {
        val searchQuery = "%$query"
        noteViewModel.searchNote(searchQuery).observe(
            this,
            { list -> noteAdapter.differ.submitList(list) }
        )
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null) {
            searchNote(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}