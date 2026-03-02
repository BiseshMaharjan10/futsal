package com.example.futsal.viewmodel

import androidx.lifecycle.ViewModel
import com.example.futsal.model.AssignmentModel
import com.example.futsal.repository.AssignmentRepo
import com.example.futsal.repository.AssignmentRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AssignmentViewModel(private val repo: AssignmentRepo = AssignmentRepoImpl()) : ViewModel() {

    private val _assignments = MutableStateFlow<List<AssignmentModel>>(emptyList())
    val assignments: StateFlow<List<AssignmentModel>> = _assignments

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        fetchAssignments()
    }

    fun fetchAssignments() {
        _isLoading.value = true
        repo.getAssignments { list, error ->
            _isLoading.value = false
            if (list != null) {
                _assignments.value = list
            } else {
                _message.value = error
            }
        }
    }

    fun addAssignment(assignment: AssignmentModel) {
        repo.addAssignment(assignment) { success, msg ->
            if (success) {
                _message.value = "Assignment added successfully"
            } else {
                _message.value = msg
            }
        }
    }

    fun updateAssignment(id: String, assignment: Map<String, Any?>) {
        repo.updateAssignment(id, assignment) { success, msg ->
            if (success) {
                _message.value = "Assignment updated successfully"
            } else {
                _message.value = msg
            }
        }
    }

    fun deleteAssignment(id: String) {
        repo.deleteAssignment(id) { success, msg ->
            if (success) {
                _message.value = "Assignment deleted successfully"
            } else {
                _message.value = msg
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
