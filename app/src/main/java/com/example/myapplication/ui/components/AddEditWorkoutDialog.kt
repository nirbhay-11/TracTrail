package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.db.WorkoutExerciseEntity
import com.example.myapplication.data.model.WorkoutCategory

data class TempExercise(
    val name: String = "",
    val sets: String = "3",
    val repsOrDistance: String = "10 reps",
    val weightKg: String = ""
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditWorkoutDialog(
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        category: WorkoutCategory,
        durationMinutes: Int,
        caloriesBurned: Int,
        notes: String,
        exercises: List<WorkoutExerciseEntity>
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(WorkoutCategory.STRENGTH) }
    var durationText by remember { mutableStateOf("30") }
    var caloriesText by remember { mutableStateOf("250") }
    var notes by remember { mutableStateOf("") }

    val exerciseList = remember {
        mutableStateListOf(
            TempExercise(name = "Push Ups", sets = "3", repsOrDistance = "12 reps", weightKg = ""),
            TempExercise(name = "Bodyweight Squats", sets = "3", repsOrDistance = "15 reps", weightKg = "")
        )
    }

    var titleError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log New Workout",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Workout Title
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    label = { Text("Workout Name *") },
                    placeholder = { Text("e.g. Upper Body Strength, Morning Run") },
                    isError = titleError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Workout Category
                Column {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WorkoutCategory.entries.forEach { category ->
                            val isSelected = category == selectedCategory
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) category.composeColor
                                        else category.composeColor.copy(alpha = 0.12f)
                                    )
                                    .clickable { selectedCategory = category }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = category.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) Color.White else category.composeColor
                                )
                            }
                        }
                    }
                }

                // Duration and Calories
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = durationText,
                        onValueChange = { durationText = it },
                        label = { Text("Duration (mins)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = caloriesText,
                        onValueChange = { caloriesText = it },
                        label = { Text("Calories (kcal)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Goal (Optional)") },
                    placeholder = { Text("Felt energized, great pace!") },
                    singleLine = false,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                // Exercises Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Exercises (${exerciseList.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    OutlinedButton(
                        onClick = {
                            exerciseList.add(TempExercise())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Exercise",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add")
                    }
                }

                // Exercise Items List
                exerciseList.forEachIndexed { index, exercise ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Exercise #${index + 1}",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (exerciseList.size > 1) {
                                    IconButton(
                                        onClick = { exerciseList.removeAt(index) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = exercise.name,
                                onValueChange = { exerciseList[index] = exercise.copy(name = it) },
                                label = { Text("Exercise Name") },
                                placeholder = { Text("e.g. Bench Press, Squat") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = exercise.sets,
                                    onValueChange = { exerciseList[index] = exercise.copy(sets = it) },
                                    label = { Text("Sets") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = exercise.repsOrDistance,
                                    onValueChange = { exerciseList[index] = exercise.copy(repsOrDistance = it) },
                                    label = { Text("Reps/Distance") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1.5f)
                                )

                                OutlinedTextField(
                                    value = exercise.weightKg,
                                    onValueChange = { exerciseList[index] = exercise.copy(weightKg = it) },
                                    label = { Text("Weight (kg)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1.2f)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                        return@Button
                    }

                    val duration = durationText.toIntOrNull() ?: 30
                    val calories = caloriesText.toIntOrNull() ?: 200

                    val mappedExercises = exerciseList
                        .filter { it.name.isNotBlank() }
                        .map {
                            WorkoutExerciseEntity(
                                workoutId = 0,
                                name = it.name.ifBlank { "Exercise" },
                                sets = it.sets.toIntOrNull() ?: 3,
                                repsOrDistance = it.repsOrDistance.ifBlank { "10 reps" },
                                weightKg = it.weightKg.toDoubleOrNull(),
                                isCompleted = false
                            )
                        }

                    onSave(
                        title.trim(),
                        selectedCategory,
                        duration,
                        calories,
                        notes.trim(),
                        mappedExercises
                    )
                }
            ) {
                Text("Save Workout")
            }
        },
        dismissButton = {
            ButtonDefaults.textButtonColors()
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
