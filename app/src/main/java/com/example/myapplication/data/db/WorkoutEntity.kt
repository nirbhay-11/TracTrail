package com.example.myapplication.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.data.model.WorkoutCategory

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: WorkoutCategory,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val date: String, // ISO YYYY-MM-DD
    val notes: String = "",
    val isCompleted: Boolean = false
)
