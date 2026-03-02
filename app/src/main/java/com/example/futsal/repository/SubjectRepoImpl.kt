package com.example.futsal.repository

import com.example.futsal.model.SubjectModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SubjectRepoImpl : SubjectRepo {
    private val database = FirebaseDatabase.getInstance().getReference("subjects")

    override fun addSubject(subject: SubjectModel, callback: (Boolean, String?) -> Unit) {
        val id = database.push().key ?: return callback(false, "Failed to generate ID")
        val newSubject = subject.copy(id = id)
        database.child(id).setValue(newSubject).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Subject added successfully")
            } else {
                callback(false, it.exception?.message)
            }
        }
    }

    override fun updateSubject(id: String, subject: Map<String, Any?>, callback: (Boolean, String?) -> Unit) {
        database.child(id).updateChildren(subject).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Subject updated successfully")
            } else {
                callback(false, it.exception?.message)
            }
        }
    }

    override fun deleteSubject(id: String, callback: (Boolean, String?) -> Unit) {
        database.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Subject deleted successfully")
            } else {
                callback(false, it.exception?.message)
            }
        }
    }

    override fun getSubjects(callback: (List<SubjectModel>?, String?) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val subjects = mutableListOf<SubjectModel>()
                for (child in snapshot.children) {
                    val subject = child.getValue(SubjectModel::class.java)
                    if (subject != null) {
                        subjects.add(subject)
                    }
                }
                callback(subjects, null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
    }
}
