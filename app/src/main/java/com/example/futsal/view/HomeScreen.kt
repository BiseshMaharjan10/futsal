package com.example.futsal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.futsal.model.SubjectModel
import com.example.futsal.repository.SubjectRepoImpl
import com.example.futsal.viewmodel.SubjectViewModel
import com.example.futsal.viewmodel.factory.SubjectViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onEditClick: (SubjectModel) -> Unit
) {
    val context = LocalContext.current
    val viewModel: SubjectViewModel = viewModel(
        factory = SubjectViewModelFactory(SubjectRepoImpl(context))
    )
    val subjects by viewModel.subjects.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Subject")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (subjects.isEmpty()) {
                Text(
                    text = "No subjects found",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(subjects) { subject ->
                        SubjectItem(
                            subject = subject,
                            onEdit = { onEditClick(subject) },
                            onDelete = { viewModel.deleteSubject(subject.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectItem(
    subject: SubjectModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display Book Image
            if (subject.bookImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(subject.bookImageUrl),
                    contentDescription = "Book Image",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 16.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", fontSize = 10.sp)
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.subjectName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Teacher: ${subject.teacherName}", fontSize = 14.sp)
                Text(text = "Credit Hours: ${subject.creditHours}", fontSize = 14.sp)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Blue)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}
