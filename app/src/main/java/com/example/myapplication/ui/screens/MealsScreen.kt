package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.MealType
import com.example.myapplication.ui.components.AddEditMealDialog
import com.example.myapplication.ui.components.MealCard
import com.example.myapplication.ui.components.NutritionSummaryCard
import com.example.myapplication.ui.components.WeekDateStrip
import com.example.myapplication.ui.viewmodel.MealViewModel

@Composable
fun MealsScreen(
    viewModel: MealViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val selectedMealType by viewModel.selectedMealTypeFilter.collectAsState()

    val totalCalories by viewModel.totalCalories.collectAsState()
    val totalProtein by viewModel.totalProtein.collectAsState()
    val totalCarbs by viewModel.totalCarbs.collectAsState()
    val totalFat by viewModel.totalFat.collectAsState()
    val goals by viewModel.goals.collectAsState()

    var showAddMealDialog by remember { mutableStateOf(false) }

    val filteredMeals = if (selectedMealType == null) {
        meals
    } else {
        meals.filter { it.mealType == selectedMealType }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Nutrition Overview Summary Header
            NutritionSummaryCard(
                caloriesConsumed = totalCalories,
                proteinGrams = totalProtein,
                carbsGrams = totalCarbs,
                fatGrams = totalFat,
                goals = goals
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Week Date Picker
            WeekDateStrip(
                selectedDate = selectedDate,
                onDateSelected = { date -> viewModel.selectDate(date) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Meal Type Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAllSelected = selectedMealType == null
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isAllSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { viewModel.setMealTypeFilter(null) }
                    ) {
                        Text(
                            text = "All Meals",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isAllSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }

                items(MealType.entries) { type ->
                    val isSelected = type == selectedMealType
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) type.composeColor else type.composeColor.copy(alpha = 0.15f),
                        modifier = Modifier.clickable { viewModel.setMealTypeFilter(type) }
                    ) {
                        Text(
                            text = type.displayName,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) Color.White else type.composeColor,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Meals List or Empty State
            if (filteredMeals.isEmpty()) {
                EmptyMealsState(
                    onAddMealClick = { showAddMealDialog = true },
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(
                        items = filteredMeals,
                        key = { it.id }
                    ) { meal ->
                        MealCard(
                            meal = meal,
                            onDeleteMeal = { viewModel.deleteMeal(meal) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAddMealDialog = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Log Meal"
            )
        }
    }

    // Add Meal Dialog
    if (showAddMealDialog) {
        AddEditMealDialog(
            onDismiss = { showAddMealDialog = false },
            onSave = { name, mealType, calories, protein, carbs, fat, time, notes ->
                viewModel.addMeal(
                    name = name,
                    mealType = mealType,
                    calories = calories,
                    proteinGrams = protein,
                    carbsGrams = carbs,
                    fatGrams = fat,
                    time = time,
                    notes = notes
                )
                showAddMealDialog = false
            }
        )
    }
}

@Composable
fun EmptyMealsState(
    onAddMealClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Meals Logged",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Track your breakfast, lunch, dinner, and snacks to monitor your daily calories and macros!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onAddMealClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Log Meal"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log A Meal")
        }
    }
}
