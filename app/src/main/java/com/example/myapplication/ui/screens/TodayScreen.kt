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
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import java.time.LocalDate

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
    val analytics by viewModel.analyticsSummary.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Date Strip Header
        WeekDateStrip(
            selectedDate = selectedDate,
            onDateSelected = { viewModel.selectDate(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Progress Overview Card
        ProgressOverviewCard(
            habitItems = habitItems,
            selectedDate = selectedDate,
            bestStreak = analytics.bestStreak
        )

        // Category Filter Chips Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
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

        Spacer(modifier = Modifier.height(4.dp))

        // Habit List or Empty State
        if (habitItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (selectedCategory != null) "No habits in ${selectedCategory?.displayName}" else "No active habits found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Build positive routines day by day. Tap '+' below to create your first habit!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = onAddNewHabitClick) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.padding(end = 6.dp))
                        Text("Add Habit")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = habitItems,
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
