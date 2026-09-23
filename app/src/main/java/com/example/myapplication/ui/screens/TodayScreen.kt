package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.HabitCategory
import com.example.myapplication.data.repository.HabitItemState
import com.example.myapplication.ui.components.HabitCard
import com.example.myapplication.ui.components.ProgressOverviewCard
import com.example.myapplication.ui.components.WeekDateStrip
import com.example.myapplication.ui.viewmodel.HabitViewModel

@Composable
fun TodayScreen(
    viewModel: HabitViewModel,
    onHabitSelected: (HabitItemState) -> Unit,
    onAddNewHabitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val habitItems by viewModel.habitItems.collectAsState()
    val allHabits by viewModel.allHabits.collectAsState()
    val analytics by viewModel.analyticsSummary.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0 = Active, 1 = Archived
    var searchQuery by remember { mutableStateOf("") }

    val filteredItems = habitItems.filter { itemState ->
        val habit = itemState.habit
        val matchesTab = if (selectedTabIndex == 0) !habit.isArchived else habit.isArchived
        val matchesSearch = searchQuery.isBlank() ||
                habit.name.contains(searchQuery, ignoreCase = true) ||
                habit.description.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == null || habit.category == selectedCategory
        matchesTab && matchesSearch && matchesCategory
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Week Date Strip Header
        WeekDateStrip(
            selectedDate = selectedDate,
            onDateSelected = { viewModel.selectDate(it) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Progress Overview Banner
        ProgressOverviewCard(
            habitItems = habitItems,
            selectedDate = selectedDate,
            bestStreak = analytics.bestStreak
        )

        // Active vs Archived Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Active Habits (${allHabits.count { !it.isArchived }})") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Archived (${allHabits.count { it.isArchived }})") }
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search habits...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.selectCategory(null) },
                    label = { Text("All") }
                )
            }
            items(HabitCategory.values()) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        if (selectedCategory == category) {
                            viewModel.selectCategory(null)
                        } else {
                            viewModel.selectCategory(category)
                        }
                    },
                    label = { Text(category.displayName) }
                )
            }
        }

        // Habit Cards List or Empty State
        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (selectedTabIndex == 1) "No archived habits" else "No habits found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Build positive daily routines. Tap 'Add Habit' below to log a new habit!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = onAddNewHabitClick) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.padding(end = 6.dp))
                        Text("Add Habit")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = filteredItems,
                    key = { it.habit.id }
                ) { itemState ->
                    HabitCard(
                        itemState = itemState,
                        onToggleCompletion = { viewModel.toggleHabitCompletion(itemState.habit) },
                        onIncrement = { viewModel.incrementProgress(itemState.habit) },
                        onDecrement = { viewModel.decrementProgress(itemState.habit) },
                        onClick = { onHabitSelected(itemState) }
                    )
                }
            }
        }
    }
}
