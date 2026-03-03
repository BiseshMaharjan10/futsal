package com.example.futsal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.futsal.ui.theme.Green
import com.example.futsal.ui.theme.Lightgray
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
        topBar = {
            TopAppBar(
                title = { Text("My Subjects", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                containerColor = Green,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Subject")
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Green)
            } else if (subjects.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No subjects yet", color = Color.Gray, fontSize = 18.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Styled Image
            if (subject.bookImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(subject.bookImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Lightgray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.subjectName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = subject.teacherName,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Green.copy(alpha = 0.1f),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "${subject.creditHours} Credits",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 12.sp,
                        color = Green,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF4285F4))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEA4335))
                }
            }
        }
    }
}
