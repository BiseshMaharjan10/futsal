package com.example.futsal.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.futsal.repository.SubjectRepo
import com.example.futsal.viewmodel.SubjectViewModel

class SubjectViewModelFactory(private val repo: SubjectRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectViewModel::class.java)) {
            return SubjectViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
