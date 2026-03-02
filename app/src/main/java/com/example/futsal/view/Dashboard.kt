package com.example.futsal.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.res.painterResource
import com.example.futsal.R
import com.example.futsal.model.AssignmentModel

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
    data class NavItem(val label: String, val icon: Int?, val vectorIcon: androidx.compose.ui.graphics.vector.ImageVector? = null)

    var selectedIndex by remember { mutableIntStateOf(0) }
    var currentSubScreen by remember { mutableStateOf("list") }
    var selectedAssignment by remember { mutableStateOf<AssignmentModel?>(null) }

    val listNav = listOf(
        NavItem(label = "Home", icon = R.drawable.baseline_home_24),
        NavItem(label = "Assignments", icon = null, vectorIcon = Icons.Default.List)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listNav.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            if (item.vectorIcon != null) {
                                Icon(item.vectorIcon, contentDescription = null)
                            } else if (item.icon != null) {
                                Icon(painter = painterResource(item.icon), contentDescription = null)
                            }
                        },
                        label = { Text(item.label) },
                        onClick = {
                            selectedIndex = index
                            if (index == 1) currentSubScreen = "list"
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
                0 -> HomeScreen()
                1 -> {
                    when (currentSubScreen) {
                        "list" -> {
                            AssignmentListScreen(
                                onAddClick = {
                                    selectedAssignment = null
                                    currentSubScreen = "add_edit"
                                },
                                onEditClick = { assignment ->
                                    selectedAssignment = assignment
                                    currentSubScreen = "add_edit"
                                }
                            )
                        }
                        "add_edit" -> {
                            AddEditAssignmentScreen(
                                assignment = selectedAssignment,
                                onBack = {
                                    currentSubScreen = "list"
                                }
                            )
                        }
                    }
                }
                else -> HomeScreen()
            }
        }
    }
}
