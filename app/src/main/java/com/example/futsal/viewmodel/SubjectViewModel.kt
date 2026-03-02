package com.example.futsal.viewmodel

import androidx.lifecycle.ViewModel
import com.example.futsal.model.SubjectModel
import com.example.futsal.repository.SubjectRepo
import com.example.futsal.repository.SubjectRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SubjectViewModel(private val repo: SubjectRepo = SubjectRepoImpl()) : ViewModel() {

    private val _subjects = MutableStateFlow<List<SubjectModel>>(emptyList())
    val subjects: StateFlow<List<SubjectModel>> = _subjects

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        fetchSubjects()
    }

    fun fetchSubjects() {
        _isLoading.value = true
        repo.getSubjects { list, error ->
            _isLoading.value = false
            if (list != null) {
                _subjects.value = list
            } else {
                _message.value = error
            }
        }
    }

    fun addSubject(subject: SubjectModel) {
        repo.addSubject(subject) { success, msg ->
            if (success) {
                _message.value = "Subject added successfully"
            } else {
                _message.value = msg
            }
        }
    }

    fun updateSubject(id: String, subject: Map<String, Any?>) {
        repo.updateSubject(id, subject) { success, msg ->
            if (success) {
                _message.value = "Subject updated successfully"
            } else {
                _message.value = msg
            }
        }
    }

    fun deleteSubject(id: String) {
        repo.deleteSubject(id) { success, msg ->
            if (success) {
                _message.value = "Subject deleted successfully"
            } else {
                _message.value = msg
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
