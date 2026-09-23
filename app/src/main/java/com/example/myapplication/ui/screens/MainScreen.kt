package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.myapplication.ui.theme.ThemeMode
import com.example.myapplication.ui.viewmodel.HabitViewModel
import com.example.myapplication.ui.viewmodel.MealViewModel
import com.example.myapplication.ui.viewmodel.ThemeViewModel
import com.example.myapplication.ui.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    habitViewModel: HabitViewModel,
    workoutViewModel: WorkoutViewModel,
    mealViewModel: MealViewModel,
    themeViewModel: ThemeViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Today, 1 = Habits, 2 = Workouts, 3 = Meals, 4 = Analytics

    val currentThemeMode by themeViewModel.themeMode.collectAsState()
    var showThemeMenu by remember { mutableStateOf(false) }

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
                            2 -> "Workout Tracker"
                            3 -> "Meal & Nutrition Tracker"
                            else -> "Analytics"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Theme Switcher Button
                    IconButton(onClick = { showThemeMenu = true }) {
                        Icon(
                            imageVector = when (currentThemeMode) {
                                ThemeMode.LIGHT -> Icons.Default.LightMode
                                ThemeMode.DARK -> Icons.Default.DarkMode
                                ThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
                            },
                            contentDescription = "Switch Theme"
                        )
                    }

                    // Theme Selection Dropdown Menu
                    DropdownMenu(
                        expanded = showThemeMenu,
                        onDismissRequest = { showThemeMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("🌓 System Default") },
                            onClick = {
                                themeViewModel.setThemeMode(ThemeMode.SYSTEM)
                                showThemeMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("☀️ Light Mode") },
                            onClick = {
                                themeViewModel.setThemeMode(ThemeMode.LIGHT)
                                showThemeMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("🌙 Dark Mode") },
                            onClick = {
                                themeViewModel.setThemeMode(ThemeMode.DARK)
                                showThemeMenu = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
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
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workouts") },
                    label = { Text("Workouts") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Restaurant, contentDescription = "Meals") },
                    label = { Text("Meals") }
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Analytics") },
                    label = { Text("Analytics") }
                )
            }
        },
        floatingActionButton = {
            if (selectedTab == 0 || selectedTab == 1) {
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
                viewModel = habitViewModel,
                onHabitSelected = { selectedDetailItem = it },
                onAddNewHabitClick = {
                    habitToEdit = null
                    showAddEditDialog = true
                },
                modifier = Modifier.padding(innerPadding)
            )
            1 -> HabitListScreen(
                viewModel = habitViewModel,
                onEditHabit = { habit ->
                    habitToEdit = habit
                    showAddEditDialog = true
                },
                modifier = Modifier.padding(innerPadding)
            )
            2 -> WorkoutsScreen(
                viewModel = workoutViewModel,
                modifier = Modifier.padding(innerPadding)
            )
            3 -> MealsScreen(
                viewModel = mealViewModel,
                modifier = Modifier.padding(innerPadding)
            )
            4 -> AnalyticsScreen(
                viewModel = habitViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    // Add / Edit Habit Dialog
    if (showAddEditDialog) {
        AddEditHabitDialog(
            habitToEdit = habitToEdit,
            onDismiss = { showAddEditDialog = false },
            onSave = { name, description, category, targetCount, unit, frequency, colorHex, iconName ->
                if (habitToEdit == null) {
                    habitViewModel.addHabit(
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
                    habitViewModel.updateHabit(
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
                habitViewModel.toggleArchiveHabit(detailItem.habit)
                selectedDetailItem = null
            },
            onDelete = {
                habitViewModel.deleteHabit(detailItem.habit)
                selectedDetailItem = null
            }
        )
    }
}
