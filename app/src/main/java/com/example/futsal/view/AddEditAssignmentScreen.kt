package com.example.futsal.view

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.futsal.model.AssignmentModel
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
            onBack()
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (assignment == null) "Add Assignment" else "Edit Assignment") },
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
                value = title,
                onValueChange = { title = it },
                label = { Text("Assignment Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Date Picker Field
            Box(modifier = Modifier.fillMaxWidth().clickable { datepicker.show() }) {
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { },
                    label = { Text("Due Date") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Status:")
            Row {
                RadioButton(
                    selected = status == "Pending",
                    onClick = { status = "Pending" }
                )
                Text("Pending", modifier = Modifier.padding(top = 12.dp))
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = status == "Completed",
                    onClick = { status = "Completed" }
                )
                Text("Completed", modifier = Modifier.padding(top = 12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (assignment == null) "Add" else "Update")
            }
        }
    }
}
