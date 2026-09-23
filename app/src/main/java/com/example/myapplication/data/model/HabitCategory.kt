package com.example.myapplication.data.model

import androidx.compose.ui.graphics.Color

enum class HabitCategory(val displayName: String, val colorHex: Long) {
    HEALTH("Health", 0xFF4CAF50),
    FITNESS("Fitness", 0xFFFF5722),
    PRODUCTIVITY("Productivity", 0xFF2196F3),
    MINDFULNESS("Mindfulness", 0xFF9C27B0),
    LEARNING("Learning", 0xFFFF9800),
    FINANCE("Finance", 0xFF009688),
    CUSTOM("Custom", 0xFF607D8B);

    fun getColor(): Color = Color(colorHex)
}
