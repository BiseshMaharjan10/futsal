package com.example.futsal.repository

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.futsal.model.SubjectModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SubjectRepoImpl(private val context: Context) : SubjectRepo {
    private val database = FirebaseDatabase.getInstance().getReference("subjects")

    init {
        try {
            val config = mapOf(
                "cloud_name" to "dusnktkk7",
                "api_key" to "597162626537592",
                "api_secret" to "QCNxWNXYobaiZ9aO9Nm6AoPG-ME"
            )
            MediaManager.init(context, config)
        } catch (e: Exception) {
            // MediaManager already initialized
        }
    }

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

    fun uploadImage(uri: Uri, callback: (String?) -> Unit) {
        MediaManager.get().upload(uri)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val url = resultData?.get("secure_url") as? String
                    callback(url)
                }
                override fun onError(requestId: String?, error: ErrorInfo?) {
                    callback(null)
                }
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            }).dispatch()
    }
}
