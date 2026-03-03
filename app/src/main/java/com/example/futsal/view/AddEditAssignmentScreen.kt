package com.example.futsal.view

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.futsal.model.AssignmentModel
import com.example.futsal.ui.theme.Green
import com.example.futsal.ui.theme.Lightgray
import com.example.futsal.viewmodel.AssignmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAssignmentScreen(
    assignment: AssignmentModel? = null,
    onBack: () -> Unit,
    viewModel: AssignmentViewModel = viewModel()
) {
    var title by remember { mutableStateOf(assignment?.assignmentTitle ?: "") }
    var subject by remember { mutableStateOf(assignment?.subjectName ?: "") }
    var dueDate by remember { mutableStateOf(assignment?.dueDate ?: "") }
    var status by remember { mutableStateOf(assignment?.status ?: "Pending") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datepicker = DatePickerDialog(
        context, { _, y, m, d ->
            dueDate = "$y/${m + 1}/$d"
        }, year, month, day
    )

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
                        if (assignment == null) "Add Assignment" else "Edit Assignment",
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
            Icon(
                imageVector = Icons.Default.Topic,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Green
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Assignment Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green,
                    focusedLabelColor = Green
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green,
                    focusedLabelColor = Green
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Styled Date Picker Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datepicker.show() }
            ) {
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { },
                    label = { Text("Due Date") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Green)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Status",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = status == "Pending",
                        onClick = { status = "Pending" },
                        colors = RadioButtonDefaults.colors(selectedColor = Green)
                    )
                    Text("Pending", fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(24.dp))
                    RadioButton(
                        selected = status == "Completed",
                        onClick = { status = "Completed" },
                        colors = RadioButtonDefaults.colors(selectedColor = Green)
                    )
                    Text("Completed", fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.isBlank() || subject.isBlank() || dueDate.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (assignment == null) {
                        viewModel.addAssignment(
                            AssignmentModel(
                                assignmentTitle = title,
                                subjectName = subject,
                                dueDate = dueDate,
                                status = status
                            )
                        )
                    } else {
                        viewModel.updateAssignment(
                            assignment.id,
                            mapOf(
                                "assignmentTitle" to title,
                                "subjectName" to subject,
                                "dueDate" to dueDate,
                                "status" to status
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Text(
                    if (assignment == null) "Add Assignment" else "Update Assignment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
