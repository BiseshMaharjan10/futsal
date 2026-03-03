package com.example.futsal.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
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
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            onBack()
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        if (subject == null) "Add Subject" else "Edit Subject",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Enhanced Image Picker UI
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Lightgray)
                    .border(2.dp, Green.copy(alpha = 0.5f), CircleShape)
                    .clickable { galleryLauncher.launch("image/*") },
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
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Green
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Add Photo",
                            fontSize = 12.sp,
                            color = Green,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Subject Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green,
                    focusedLabelColor = Green
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = teacher,
                onValueChange = { teacher = it },
                label = { Text("Teacher Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green,
                    focusedLabelColor = Green
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = creditHours,
                onValueChange = { creditHours = it },
                label = { Text("Credit Hours") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green,
                    focusedLabelColor = Green
                )
            )
            
            Spacer(modifier = Modifier.weight(1f))

            if (isUploading) {
                CircularProgressIndicator(color = Green)
            } else {
                Button(
                    onClick = {
                        if (name.isBlank() || teacher.isBlank() || creditHours.isBlank()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        if (selectedImageUri != null) {
                            isUploading = true
                            viewModel.uploadImage(selectedImageUri!!) { url ->
                                isUploading = false
                                if (url != null) {
                                    saveSubject(viewModel, subject, name, teacher, creditHours, url)
                                } else {
                                    Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            saveSubject(viewModel, subject, name, teacher, creditHours, bookImageUrl)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green)
                ) {
                    Text(
                        if (subject == null) "Create Subject" else "Update Subject",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
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
