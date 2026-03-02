package com.example.futsal.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.futsal.model.AssignmentModel
import com.example.futsal.model.SubjectModel

class Dashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}

@Composable
fun DashboardBody() {
    data class NavItem(val label: String, val vectorIcon: androidx.compose.ui.graphics.vector.ImageVector)

    var selectedIndex by remember { mutableIntStateOf(0) }
    
    // Assignment navigation state
    var currentAssignmentSubScreen by remember { mutableStateOf("list") }
    var selectedAssignment by remember { mutableStateOf<AssignmentModel?>(null) }
    
    // Subject navigation state (now on Home tab)
    var currentSubjectSubScreen by remember { mutableStateOf("list") }
    var selectedSubject by remember { mutableStateOf<SubjectModel?>(null) }

    val listNav = listOf(
        NavItem(label = "Home", vectorIcon = Icons.Default.Home),
        NavItem(label = "Assignments", vectorIcon = Icons.AutoMirrored.Filled.List)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listNav.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(item.vectorIcon, contentDescription = null)
                        },
                        label = { Text(item.label) },
                        onClick = {
                            selectedIndex = index
                            // Reset sub-screens when switching tabs
                            if (index == 0) currentSubjectSubScreen = "list"
                            if (index == 1) currentAssignmentSubScreen = "list"
                        },
                        selected = selectedIndex == index
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedIndex) {
                0 -> {
                    // Home tab now shows Subject Management via HomeScreen
                    when (currentSubjectSubScreen) {
                        "list" -> {
                            HomeScreen(
                                onAddClick = {
                                    selectedSubject = null
                                    currentSubjectSubScreen = "add_edit"
                                },
                                onEditClick = { subject ->
                                    selectedSubject = subject
                                    currentSubjectSubScreen = "add_edit"
                                }
                            )
                        }
                        "add_edit" -> {
                            AddEditSubjectScreen(
                                subject = selectedSubject,
                                onBack = {
                                    currentSubjectSubScreen = "list"
                                }
                            )
                        }
                    }
                }
                1 -> {
                    when (currentAssignmentSubScreen) {
                        "list" -> {
                            AssignmentListScreen(
                                onAddClick = {
                                    selectedAssignment = null
                                    currentAssignmentSubScreen = "add_edit"
                                },
                                onEditClick = { assignment ->
                                    selectedAssignment = assignment
                                    currentAssignmentSubScreen = "add_edit"
                                }
                            )
                        }
                        "add_edit" -> {
                            AddEditAssignmentScreen(
                                assignment = selectedAssignment,
                                onBack = {
                                    currentAssignmentSubScreen = "list"
                                }
                            )
                        }
                    }
                }
                else -> {
                    // Fallback
                    Text("Select a tab")
                }
            }
        }
    }
}
