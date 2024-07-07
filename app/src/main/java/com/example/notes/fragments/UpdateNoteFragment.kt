package com.example.notes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.MainActivity
import com.example.notes.R
import com.example.notes.databinding.FragmentUpdateNoteBinding
import com.example.notes.model.Note
import com.example.notes.viewmodel.NoteViewModel

class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var currentNote: Note
    private val args: UpdateNoteFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateNoteBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.noteUpdateTitleEditText.setText(currentNote.noteTitle)
        binding.noteUpdateBodyEditText.setText(currentNote.noteBody)

        //if update
        binding.doneButton.setOnClickListener {
            val noteTitle = binding.noteUpdateTitleEditText.text.toString().trim()
            val noteBody = binding.noteUpdateBodyEditText.text.toString().trim()

            if (noteTitle.isNotEmpty()){
                val note = Note(currentNote.id, noteTitle, noteBody)
                noteViewModel.updateNote(note)

                Toast.makeText(
                    context,
                    "Note $noteTitle is successfully updated",
                    Toast.LENGTH_SHORT
                ).show()

                view.findNavController()
                    .navigate(R.id.action_updateNoteFragment_to_homeFragment)
            }else{
                Toast.makeText(
                    context,
                    "Please enter note Title",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun deleteNote(){

        activity?.let {
            AlertDialog.Builder(it).apply {

                setTitle("Delete Note")
                setMessage("You want to delete this Note?")
                setPositiveButton("Delete"){_,_,->
                    noteViewModel.deleteNote(currentNote)

                    view?.findNavController()
                        ?.navigate(R.id.action_updateNoteFragment_to_homeFragment)
                }
                setNegativeButton("Cancel", null)
            }.create().show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.update_note_manu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_delete -> {
                deleteNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}