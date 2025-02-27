package com.example.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notes.database.NoteDataBase
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.repository.NoteRepository
import com.example.notes.viewmodel.NoteViewModel
import com.example.notes.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
    }

    private fun setUpViewModel() {
        val noteRepository = NoteRepository(NoteDataBase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)

        noteViewModel =
            ViewModelProvider(
                this,
                viewModelProviderFactory
            ).get(NoteViewModel::class.java)
    }
}