package com.example.futsal.repository

import com.example.futsal.model.SubjectModel

interface SubjectRepo {
    fun addSubject(subject: SubjectModel, callback: (Boolean, String?) -> Unit)
    fun updateSubject(id: String, subject: Map<String, Any?>, callback: (Boolean, String?) -> Unit)
    fun deleteSubject(id: String, callback: (Boolean, String?) -> Unit)
    fun getSubjects(callback: (List<SubjectModel>?, String?) -> Unit)
}
