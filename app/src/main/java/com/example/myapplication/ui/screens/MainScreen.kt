package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.data.db.HabitEntity
import com.example.myapplication.data.repository.HabitItemState
import com.example.myapplication.ui.components.AddEditHabitDialog
import com.example.myapplication.ui.components.HabitDetailDialog
import com.example.myapplication.ui.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: HabitViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Today, 1 = Habits, 2 = Analytics

    var showAddEditDialog by remember { mutableStateOf(false) }
    var habitToEdit by remember { mutableStateOf<HabitEntity?>(null) }

    var selectedDetailItem by remember { mutableStateOf<HabitItemState?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            0 -> "Habit Tracker"
                            1 -> "My Habits"
                            else -> "Analytics"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Today") },
                    label = { Text("Today") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Habits") },
                    label = { Text("Habits") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Analytics") },
                    label = { Text("Analytics") }
                )
            }
        },
        floatingActionButton = {
            if (selectedTab != 2) {
                FloatingActionButton(
                    onClick = {
                        habitToEdit = null
                        showAddEditDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Habit")
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> TodayScreen(
                viewModel = viewModel,
                onHabitSelected = { selectedDetailItem = it },
                onAddNewHabitClick = {
                    habitToEdit = null
                    showAddEditDialog = true
                },
                modifier = Modifier.padding(innerPadding)
            )
            1 -> HabitListScreen(
                viewModel = viewModel,
                onEditHabit = { habit ->
                    habitToEdit = habit
                    showAddEditDialog = true
                },
                modifier = Modifier.padding(innerPadding)
            )
            2 -> AnalyticsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    // Add / Edit Dialog
    if (showAddEditDialog) {
        AddEditHabitDialog(
            habitToEdit = habitToEdit,
            onDismiss = { showAddEditDialog = false },
            onSave = { name, description, category, targetCount, unit, frequency, colorHex, iconName ->
                if (habitToEdit == null) {
                    viewModel.addHabit(
                        name,
                        description,
                        category,
                        targetCount,
                        unit,
                        frequency,
                        colorHex,
                        iconName
                    )
                } else {
                    viewModel.updateHabit(
                        habitToEdit!!.copy(
                            name = name,
                            description = description,
                            category = category,
                            targetCount = targetCount,
                            unit = unit,
                            frequency = frequency,
                            colorHex = colorHex,
                            iconName = iconName
                        )
                    )
                }
                showAddEditDialog = false
            }
        )
    }

    // Detail Dialog
    selectedDetailItem?.let { detailItem ->
        HabitDetailDialog(
            itemState = detailItem,
            onDismiss = { selectedDetailItem = null },
            onEdit = {
                habitToEdit = detailItem.habit
                selectedDetailItem = null
                showAddEditDialog = true
            },
            onArchiveToggle = {
                viewModel.toggleArchiveHabit(detailItem.habit)
                selectedDetailItem = null
            },
            onDelete = {
                viewModel.deleteHabit(detailItem.habit)
                selectedDetailItem = null
            }
        )
    }
}
