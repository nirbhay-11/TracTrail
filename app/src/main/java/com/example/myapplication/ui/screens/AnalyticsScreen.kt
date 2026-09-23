package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.repository.DailyCompletionStat
import com.example.myapplication.ui.viewmodel.HabitViewModel
import com.example.myapplication.ui.viewmodel.MealViewModel
import com.example.myapplication.ui.viewmodel.WorkoutViewModel

@Composable
fun AnalyticsScreen(
    habitViewModel: HabitViewModel,
    workoutViewModel: WorkoutViewModel,
    mealViewModel: MealViewModel,
    modifier: Modifier = Modifier
) {
    val habitAnalytics by habitViewModel.analyticsSummary.collectAsState()
    val totalCaloriesBurned by workoutViewModel.totalCalories.collectAsState()
    val totalWorkoutMinutes by workoutViewModel.totalMinutes.collectAsState()
    val completedWorkouts by workoutViewModel.totalCompletedWorkouts.collectAsState()

    val totalCaloriesConsumed by mealViewModel.totalCalories.collectAsState()
    val totalProtein by mealViewModel.totalProtein.collectAsState()

    var selectedRangeTab by remember { mutableIntStateOf(0) } // 0 = Week, 1 = Month, 2 = All Time

    val habitRate = habitAnalytics.completionRateToday.coerceIn(0f, 1f)
    val workoutFactor = if (completedWorkouts > 0) 0.3f else 0.1f
    val nutritionFactor = if (totalCaloriesConsumed > 0) 0.3f else 0.1f
    val consistencyScore = ((habitRate * 0.4f + workoutFactor + nutritionFactor) * 100).toInt().coerceIn(35, 98)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Time Range Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Analytics & Insights",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Week", "Month", "All Time").forEachIndexed { index, label ->
                    val isSelected = selectedRangeTab == index
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedRangeTab = index }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Clean Consistency Score Indicator
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Consistency Index",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = when {
                            consistencyScore >= 80 -> "Stellar momentum! Excellent consistency."
                            consistencyScore >= 60 -> "Solid routine! Sticking to your daily targets."
                            else -> "Keep building daily routines step by step."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "$consistencyScore / 100",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (consistencyScore / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        // Key Metric Grid (Clean Cards, No artificial icons)
        Text(
            text = "Key Metrics",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CleanMetricCard(
                title = "Best Streak",
                value = "${habitAnalytics.bestStreak} Days",
                subtitle = habitAnalytics.topStreakHabitName.ifBlank { "Habit Streak" },
                modifier = Modifier.weight(1f)
            )

            CleanMetricCard(
                title = "Habit Logs",
                value = "${habitAnalytics.totalCompletionsAllTime}",
                subtitle = "Total completed",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CleanMetricCard(
                title = "Workout Time",
                value = "${totalWorkoutMinutes}m",
                subtitle = "$completedWorkouts sessions done",
                modifier = Modifier.weight(1f)
            )

            CleanMetricCard(
                title = "Calories Burned",
                value = "$totalCaloriesBurned kcal",
                subtitle = "Active energy burned",
                modifier = Modifier.weight(1f)
            )
        }

        // 7-Day Completion Bar Chart
        Text(
            text = "7-Day Habit Activity",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                WeeklyBarChart(stats = habitAnalytics.weeklyCompletionData)
            }
        }

        // Genuine Activity Summary
        Text(
            text = "Personal Activity Summary",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryRow(
                    label = "Habits Progress",
                    detail = if (habitAnalytics.bestStreak > 0)
                        "Your top streak is ${habitAnalytics.bestStreak} days (${habitAnalytics.topStreakHabitName})."
                    else "Active habits are tracked daily on your schedule."
                )

                SummaryRow(
                    label = "Fitness Tracking",
                    detail = if (totalWorkoutMinutes > 0)
                        "Logged $totalWorkoutMinutes minutes of active workouts ($totalCaloriesBurned kcal burned)."
                    else "Log your workouts in the Workouts tab to track training time."
                )

                SummaryRow(
                    label = "Nutrition Intake",
                    detail = if (totalCaloriesConsumed > 0)
                        "Logged $totalCaloriesConsumed kcal with ${totalProtein.toInt()}g protein."
                    else "Log meals to track daily protein, carbs, and calories."
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CleanMetricCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeeklyBarChart(
    stats: List<DailyCompletionStat>
) {
    val maxCount = (stats.maxOfOrNull { it.completedCount } ?: 1).coerceAtLeast(1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        stats.forEach { stat ->
            val fillRatio = stat.completedCount.toFloat() / maxCount.toFloat()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${stat.completedCount}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fillRatio.coerceIn(0.08f, 1.0f))
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (stat.completedCount > 0) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stat.dayName,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    detail: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = detail,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
