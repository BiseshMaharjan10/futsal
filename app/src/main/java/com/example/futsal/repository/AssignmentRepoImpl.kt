package com.example.futsal.repository

import com.example.futsal.model.AssignmentModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AssignmentRepoImpl : AssignmentRepo {
    private val database = FirebaseDatabase.getInstance().getReference("assignments")

    override fun addAssignment(assignment: AssignmentModel, callback: (Boolean, String?) -> Unit) {
        val id = database.push().key ?: return callback(false, "Failed to generate ID")
        val newAssignment = assignment.copy(id = id)
        database.child(id).setValue(newAssignment).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Assignment added successfully")
            } else {
                callback(false, it.exception?.message)
            }
        }
    }

    override fun updateAssignment(id: String, assignment: Map<String, Any?>, callback: (Boolean, String?) -> Unit) {
        database.child(id).updateChildren(assignment).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Assignment updated successfully")
            } else {
                callback(false, it.exception?.message)
            }
        }
    }

    override fun deleteAssignment(id: String, callback: (Boolean, String?) -> Unit) {
        database.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Assignment deleted successfully")
            } else {
                callback(false, it.exception?.message)
            }
        }
    }

    override fun getAssignments(callback: (List<AssignmentModel>?, String?) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val assignments = mutableListOf<AssignmentModel>()
                for (child in snapshot.children) {
                    val assignment = child.getValue(AssignmentModel::class.java)
                    if (assignment != null) {
                        assignments.add(assignment)
                    }
                }
                callback(assignments, null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
    }
}
