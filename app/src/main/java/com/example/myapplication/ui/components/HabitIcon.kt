package com.example.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object HabitIconMapper {
    fun getIcon(name: String): ImageVector {
        return when (name) {
            "LocalWater", "LocalDrink" -> Icons.Default.LocalDrink
            "FitnessCenter" -> Icons.Default.FitnessCenter
            "Book" -> Icons.Default.Book
            "SelfImprovement" -> Icons.Default.SelfImprovement
            "Code" -> Icons.Default.Code
            "DirectionsRun" -> Icons.Default.DirectionsRun
            "NightlightRound", "Bed" -> Icons.Default.NightlightRound
            "Star" -> Icons.Default.Star
            "Fire" -> Icons.Default.LocalFireDepartment
            else -> Icons.Default.Check
        }
    }

    val availableIcons = listOf(
        "Check",
        "LocalDrink",
        "FitnessCenter",
        "Book",
        "SelfImprovement",
        "Code",
        "DirectionsRun",
        "NightlightRound",
        "Star",
        "Fire"
    )
}

@Composable
fun HabitIcon(
    iconName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    Icon(
        imageVector = HabitIconMapper.getIcon(iconName),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}
