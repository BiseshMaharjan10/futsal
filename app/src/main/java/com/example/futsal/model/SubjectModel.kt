package com.example.futsal.model

data class SubjectModel(
    val id: String = "",
    val subjectName: String = "",
    val teacherName: String = "",
    val creditHours: String = "",
    val bookImageUrl: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "subjectName" to subjectName,
            "teacherName" to teacherName,
            "creditHours" to creditHours,
            "bookImageUrl" to bookImageUrl
        )
    }
}
