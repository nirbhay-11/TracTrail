package com.example.myapplication.data.model

import androidx.compose.ui.graphics.Color

enum class WorkoutCategory(
    val displayName: String,
    val colorHex: Long,
    val iconName: String
) {
    STRENGTH("Strength Training", 0xFFFF5722, "FitnessCenter"),
    CARDIO("Cardio", 0xFF2196F3, "DirectionsRun"),
    HIIT("HIIT", 0xFFE91E63, "FlashOn"),
    YOGA("Yoga & Flex", 0xFF9C27B0, "SelfImprovement"),
    RUNNING("Running", 0xFF4CAF50, "DirectionsRun"),
    CYCLING("Cycling", 0xFFFF9800, "DirectionsBike"),
    SPORTS("Sports", 0xFF00BCD4, "SportsSoccer");

    val composeColor: Color
        get() = Color(colorHex)
}
