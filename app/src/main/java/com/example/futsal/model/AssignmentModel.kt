package com.example.futsal.model

data class AssignmentModel(
    val id: String = "",
    val subjectName: String = "",
    val assignmentTitle: String = "",
    val dueDate: String = "",
    val status: String = "Pending" // Pending / Completed
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "subjectName" to subjectName,
            "assignmentTitle" to assignmentTitle,
            "dueDate" to dueDate,
            "status" to status
        )
    }
}
