package com.example.myapplication.data.repository

import com.example.myapplication.data.db.HabitDao
import com.example.myapplication.data.db.HabitEntity
import com.example.myapplication.data.db.HabitLogEntity
import com.example.myapplication.data.db.HabitWithLogs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HabitItemState(
    val habit: HabitEntity,
    val logForSelectedDate: HabitLogEntity?,
    val currentCount: Int,
    val isCompleted: Boolean,
    val currentStreak: Int,
    val bestStreak: Int,
    val totalCompletions: Int
)

data class AnalyticsSummary(
    val totalActiveHabits: Int,
    val totalCompletionsAllTime: Int,
    val topStreakHabitName: String,
    val bestStreak: Int,
    val completionRateToday: Float,
    val weeklyCompletionData: List<DailyCompletionStat>
)

data class DailyCompletionStat(
    val date: String, // YYYY-MM-DD
    val dayName: String, // Mon, Tue, etc.
    val completedCount: Int,
    val totalHabitsCount: Int
)

class HabitRepository(private val habitDao: HabitDao) {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    val allActiveHabits: Flow<List<HabitEntity>> = habitDao.getAllActiveHabits()
    val allHabits: Flow<List<HabitEntity>> = habitDao.getAllHabits()
    val habitsWithLogs: Flow<List<HabitWithLogs>> = habitDao.getAllHabitsWithLogs()

    fun getHabitStatesForDate(dateStr: String): Flow<List<HabitItemState>> {
        return combine(
            habitDao.getAllActiveHabits(),
            habitDao.getLogsForDate(dateStr),
            habitDao.getAllLogs()
        ) { habits, logsForDate, allLogs ->
            val logsMap = logsForDate.associateBy { it.habitId }
            val logsGroupedByHabit = allLogs.groupBy { it.habitId }

            habits.map { habit ->
                val log = logsMap[habit.id]
                val habitLogs = logsGroupedByHabit[habit.id] ?: emptyList()

                val currentCount = log?.currentCount ?: 0
                val isCompleted = log?.isCompleted ?: false

                val (currentStreak, bestStreak) = calculateStreaks(habitLogs)
                val totalCompletions = habitLogs.count { it.isCompleted }

                HabitItemState(
                    habit = habit,
                    logForSelectedDate = log,
                    currentCount = currentCount,
                    isCompleted = isCompleted,
                    currentStreak = currentStreak,
                    bestStreak = bestStreak,
                    totalCompletions = totalCompletions
                )
            }
        }
    }

    val analyticsSummary: Flow<AnalyticsSummary> = habitDao.getAllHabitsWithLogs().map { habitsWithLogs ->
        val activeHabits = habitsWithLogs.filter { !it.habit.isArchived }
        val todayStr = LocalDate.now().format(dateFormatter)

        var totalCompletions = 0
        var bestStreakOverall = 0
        var topStreakHabitName = "None"
        var completedTodayCount = 0

        activeHabits.forEach { habitWithLogs ->
            val logs = habitWithLogs.logs
            totalCompletions += logs.count { it.isCompleted }

            val (curStreak, maxStreak) = calculateStreaks(logs)
            if (maxStreak > bestStreakOverall) {
                bestStreakOverall = maxStreak
                topStreakHabitName = habitWithLogs.habit.name
            }

            val todayLog = logs.find { it.date == todayStr }
            if (todayLog?.isCompleted == true) {
                completedTodayCount++
            }
        }

        val completionRateToday = if (activeHabits.isNotEmpty()) {
            completedTodayCount.toFloat() / activeHabits.size.toFloat()
        } else 0f

        // Calculate past 7 days stats
        val weeklyStats = mutableListOf<DailyCompletionStat>()
        val today = LocalDate.now()
        for (i in 6 downTo 0) {
            val targetDate = today.minusDays(i.toLong())
            val dateStr = targetDate.format(dateFormatter)
            val dayName = targetDate.dayOfWeek.name.take(3)

            var completedForDay = 0
            activeHabits.forEach { habitWithLogs ->
                val logForDay = habitWithLogs.logs.find { it.date == dateStr }
                if (logForDay?.isCompleted == true) {
                    completedForDay++
                }
            }

            weeklyStats.add(
                DailyCompletionStat(
                    date = dateStr,
                    dayName = dayName,
                    completedCount = completedForDay,
                    totalHabitsCount = activeHabits.size
                )
            )
        }

        AnalyticsSummary(
            totalActiveHabits = activeHabits.size,
            totalCompletionsAllTime = totalCompletions,
            topStreakHabitName = if (bestStreakOverall > 0) topStreakHabitName else "None",
            bestStreak = bestStreakOverall,
            completionRateToday = completionRateToday,
            weeklyCompletionData = weeklyStats
        )
    }

    suspend fun insertHabit(habit: HabitEntity): Long {
        return habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: HabitEntity) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabit(habit)
    }

    suspend fun toggleHabitCompletion(habit: HabitEntity, dateStr: String) {
        val existingLog = habitDao.getLogForDate(habit.id, dateStr)
        if (existingLog == null) {
            val newLog = HabitLogEntity(
                habitId = habit.id,
                date = dateStr,
                currentCount = habit.targetCount,
                isCompleted = true
            )
            habitDao.insertOrUpdateLog(newLog)
        } else {
            val updatedCompleted = !existingLog.isCompleted
            val updatedCount = if (updatedCompleted) habit.targetCount else 0
            val updatedLog = existingLog.copy(
                currentCount = updatedCount,
                isCompleted = updatedCompleted,
                completedAt = System.currentTimeMillis()
            )
            habitDao.insertOrUpdateLog(updatedLog)
        }
    }

    suspend fun updateHabitProgress(habit: HabitEntity, dateStr: String, delta: Int) {
        val existingLog = habitDao.getLogForDate(habit.id, dateStr)
        val current = existingLog?.currentCount ?: 0
        val newCount = (current + delta).coerceAtLeast(0)
        val isCompleted = newCount >= habit.targetCount

        val updatedLog = (existingLog ?: HabitLogEntity(
            habitId = habit.id,
            date = dateStr,
            currentCount = 0,
            isCompleted = false
        )).copy(
            currentCount = newCount,
            isCompleted = isCompleted,
            completedAt = System.currentTimeMillis()
        )

        habitDao.insertOrUpdateLog(updatedLog)
    }

    private fun calculateStreaks(logs: List<HabitLogEntity>): Pair<Int, Int> {
        val completedDates = logs.filter { it.isCompleted }
            .mapNotNull {
                try {
                    LocalDate.parse(it.date, dateFormatter)
                } catch (e: Exception) {
                    null
                }
            }
            .toSet()

        if (completedDates.isEmpty()) return Pair(0, 0)

        val today = LocalDate.now()
        var currentStreak = 0

        // Calculate current streak
        var checkDate = if (completedDates.contains(today)) today else today.minusDays(1)
        while (completedDates.contains(checkDate)) {
            currentStreak++
            checkDate = checkDate.minusDays(1)
        }

        // Calculate best (max) streak
        val sortedDates = completedDates.sorted()
        var maxStreak = 0
        var tempStreak = 0
        var previousDate: LocalDate? = null

        for (date in sortedDates) {
            if (previousDate == null || date == previousDate.plusDays(1)) {
                tempStreak++
            } else {
                tempStreak = 1
            }
            if (tempStreak > maxStreak) {
                maxStreak = tempStreak
            }
            previousDate = date
        }

        return Pair(currentStreak, maxStreak)
    }
}
