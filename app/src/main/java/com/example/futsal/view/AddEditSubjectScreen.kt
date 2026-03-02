package com.example.futsal.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.futsal.model.SubjectModel
import com.example.futsal.viewmodel.SubjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSubjectScreen(
    subject: SubjectModel? = null,
    onBack: () -> Unit,
    viewModel: SubjectViewModel = viewModel()
) {
    var name by remember { mutableStateOf(subject?.subjectName ?: "") }
    var teacher by remember { mutableStateOf(subject?.teacherName ?: "") }
    var creditHours by remember { mutableStateOf(subject?.creditHours ?: "") }

    val message by viewModel.message.collectAsState()

    LaunchedEffect(message) {
        if (message != null) {
            onBack()
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (subject == null) "Add Subject" else "Edit Subject") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Subject Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = teacher,
                onValueChange = { teacher = it },
                label = { Text("Teacher Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = creditHours,
                onValueChange = { creditHours = it },
                label = { Text("Credit Hours") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (subject == null) {
                        viewModel.addSubject(
                            SubjectModel(
                                subjectName = name,
                                teacherName = teacher,
                                creditHours = creditHours
                            )
                        )
                    } else {
                        viewModel.updateSubject(
                            subject.id,
                            mapOf(
                                "subjectName" to name,
                                "teacherName" to teacher,
                                "creditHours" to creditHours
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (subject == null) "Add" else "Update")
            }
        }
    }
}
