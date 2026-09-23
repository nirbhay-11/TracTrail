package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.db.HabitEntity
import com.example.myapplication.data.model.FrequencyType
import com.example.myapplication.data.model.HabitCategory
import com.example.myapplication.data.repository.AnalyticsSummary
import com.example.myapplication.data.repository.HabitItemState
import com.example.myapplication.data.repository.HabitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class HabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    val selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedCategory = MutableStateFlow<HabitCategory?>(null)

    val habitItems: StateFlow<List<HabitItemState>> = selectedDate
        .flatMapLatest { date ->
            repository.getHabitStatesForDate(date.format(dateFormatter))
        }
        .combine(selectedCategory) { items, category ->
            if (category == null) {
                items
            } else {
                items.filter { it.habit.category == category }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val analyticsSummary: StateFlow<AnalyticsSummary> = repository.analyticsSummary
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AnalyticsSummary(
                totalActiveHabits = 0,
                totalCompletionsAllTime = 0,
                topStreakHabitName = "None",
                bestStreak = 0,
                completionRateToday = 0f,
                weeklyCompletionData = emptyList()
            )
        )

    val allHabits: StateFlow<List<HabitEntity>> = repository.allHabits
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun selectCategory(category: HabitCategory?) {
        selectedCategory.value = category
    }

    fun toggleHabitCompletion(habit: HabitEntity) {
        viewModelScope.launch {
            val dateStr = selectedDate.value.format(dateFormatter)
            repository.toggleHabitCompletion(habit, dateStr)
        }
    }

    fun incrementProgress(habit: HabitEntity) {
        viewModelScope.launch {
            val dateStr = selectedDate.value.format(dateFormatter)
            repository.updateHabitProgress(habit, dateStr, 1)
        }
    }

    fun decrementProgress(habit: HabitEntity) {
        viewModelScope.launch {
            val dateStr = selectedDate.value.format(dateFormatter)
            repository.updateHabitProgress(habit, dateStr, -1)
        }
    }

    fun addHabit(
        name: String,
        description: String,
        category: HabitCategory,
        targetCount: Int,
        unit: String,
        frequency: FrequencyType,
        colorHex: Long,
        iconName: String
    ) {
        viewModelScope.launch {
            val habit = HabitEntity(
                name = name.ifBlank { "New Habit" },
                description = description,
                category = category,
                targetCount = targetCount.coerceAtLeast(1),
                unit = unit.ifBlank { "times" },
                frequency = frequency,
                colorHex = colorHex,
                iconName = iconName
            )
            repository.insertHabit(habit)
        }
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.updateHabit(habit)
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun toggleArchiveHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.updateHabit(habit.copy(isArchived = !habit.isArchived))
        }
    }
}
