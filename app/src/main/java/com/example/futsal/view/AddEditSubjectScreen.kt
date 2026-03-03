package com.example.futsal.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.futsal.model.SubjectModel
import com.example.futsal.repository.SubjectRepoImpl
import com.example.futsal.viewmodel.SubjectViewModel
import com.example.futsal.viewmodel.factory.SubjectViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSubjectScreen(
    subject: SubjectModel? = null,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: SubjectViewModel = viewModel(
        factory = SubjectViewModelFactory(SubjectRepoImpl(context))
    )

    var name by remember { mutableStateOf(subject?.subjectName ?: "") }
    var teacher by remember { mutableStateOf(subject?.teacherName ?: "") }
    var creditHours by remember { mutableStateOf(subject?.creditHours ?: "") }
    var bookImageUrl by remember { mutableStateOf(subject?.bookImageUrl ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Picker UI
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clickable { galleryLauncher.launch("image/*") }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (bookImageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(bookImageUrl),
                        contentDescription = "Subject Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(48.dp))
                        Text("Add Book Photo")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            if (isUploading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        if (selectedImageUri != null) {
                            isUploading = true
                            viewModel.uploadImage(selectedImageUri!!) { url ->
                                isUploading = false
                                if (url != null) {
                                    saveSubject(viewModel, subject, name, teacher, creditHours, url)
                                } else {
                                    // Handle error
                                }
                            }
                        } else {
                            saveSubject(viewModel, subject, name, teacher, creditHours, bookImageUrl)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (subject == null) "Add" else "Update")
                }
            }
        }
    }
}

private fun saveSubject(
    viewModel: SubjectViewModel,
    subject: SubjectModel?,
    name: String,
    teacher: String,
    creditHours: String,
    imageUrl: String
) {
    if (subject == null) {
        viewModel.addSubject(
            SubjectModel(
                subjectName = name,
                teacherName = teacher,
                creditHours = creditHours,
                bookImageUrl = imageUrl
            )
        )
    } else {
        viewModel.updateSubject(
            subject.id,
            mapOf(
                "subjectName" to name,
                "teacherName" to teacher,
                "creditHours" to creditHours,
                "bookImageUrl" to imageUrl
            )
        )
    }
}
