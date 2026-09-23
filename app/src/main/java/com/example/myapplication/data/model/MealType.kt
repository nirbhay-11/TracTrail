package com.example.myapplication.data.model

import androidx.compose.ui.graphics.Color

enum class MealType(
    val displayName: String,
    val colorHex: Long,
    val iconName: String
) {
    BREAKFAST("Breakfast", 0xFFFF9800, "FreeBreakfast"),
    LUNCH("Lunch", 0xFF4CAF50, "Restaurant"),
    DINNER("Dinner", 0xFF9C27B0, "DinnerDining"),
    SNACK("Snack", 0xFF00BCD4, "Apple");

    val composeColor: Color
        get() = Color(colorHex)
}
