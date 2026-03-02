package com.example.futsal.repository

import com.example.futsal.model.AssignmentModel

interface AssignmentRepo {
    fun addAssignment(assignment: AssignmentModel, callback: (Boolean, String?) -> Unit)
    fun updateAssignment(id: String, assignment: Map<String, Any?>, callback: (Boolean, String?) -> Unit)
    fun deleteAssignment(id: String, callback: (Boolean, String?) -> Unit)
    fun getAssignments(callback: (List<AssignmentModel>?, String?) -> Unit)
}
