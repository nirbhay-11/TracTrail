package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.viewmodel.NutritionGoals

@Composable
fun NutritionSummaryCard(
    caloriesConsumed: Int,
    proteinGrams: Double,
    carbsGrams: Double,
    fatGrams: Double,
    goals: NutritionGoals,
    modifier: Modifier = Modifier
) {
    val calorieProgress = (caloriesConsumed.toFloat() / goals.calorieGoal.toFloat()).coerceIn(0f, 1f)
    val proteinProgress = (proteinGrams.toFloat() / goals.proteinGoalGrams.toFloat()).coerceIn(0f, 1f)
    val carbsProgress = (carbsGrams.toFloat() / goals.carbsGoalGrams.toFloat()).coerceIn(0f, 1f)
    val fatProgress = (fatGrams.toFloat() / goals.fatGoalGrams.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Calories Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Calories Consumed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$caloriesConsumed / ${goals.calorieGoal} kcal",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = "${goals.calorieGoal - caloriesConsumed} left",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Calorie Main Progress Bar
            LinearProgressIndicator(
                progress = { calorieProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Macros Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Protein Item
                MacroItem(
                    title = "Protein",
                    amountText = "${proteinGrams.toInt()}g / ${goals.proteinGoalGrams.toInt()}g",
                    progress = proteinProgress,
                    color = Color(0xFFFF5722),
                    modifier = Modifier.weight(1f)
                )

                // Carbs Item
                MacroItem(
                    title = "Carbs",
                    amountText = "${carbsGrams.toInt()}g / ${goals.carbsGoalGrams.toInt()}g",
                    progress = carbsProgress,
                    color = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f)
                )

                // Fat Item
                MacroItem(
                    title = "Fat",
                    amountText = "${fatGrams.toInt()}g / ${goals.fatGoalGrams.toInt()}g",
                    progress = fatProgress,
                    color = Color(0xFF9C27B0),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun MacroItem(
    title: String,
    amountText: String,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = amountText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}
