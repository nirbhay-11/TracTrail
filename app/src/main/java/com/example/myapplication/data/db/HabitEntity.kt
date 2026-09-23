package com.example.myapplication.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.data.model.FrequencyType
import com.example.myapplication.data.model.HabitCategory

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val category: HabitCategory = HabitCategory.HEALTH,
    val targetCount: Int = 1,
    val unit: String = "times",
    val frequency: FrequencyType = FrequencyType.DAILY,
    val colorHex: Long = 0xFF4CAF50,
    val iconName: String = "Check",
    val createdAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)
