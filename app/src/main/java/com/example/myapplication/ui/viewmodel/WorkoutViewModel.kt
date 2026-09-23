package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.db.WorkoutEntity
import com.example.myapplication.data.db.WorkoutExerciseEntity
import com.example.myapplication.data.db.WorkoutWithExercises
import com.example.myapplication.data.model.WorkoutCategory
import com.example.myapplication.data.repository.WorkoutRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class WorkoutSummaryState(
    val totalWorkouts: Int = 0,
    val totalCalories: Int = 0,
    val totalMinutes: Int = 0
)

class WorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<WorkoutCategory?>(null)
    val selectedCategoryFilter: StateFlow<WorkoutCategory?> = _selectedCategoryFilter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val workouts: StateFlow<List<WorkoutWithExercises>> = _selectedDate
        .flatMapLatest { date ->
            repository.getWorkoutsForDate(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalCalories: StateFlow<Int> = repository.getTotalCaloriesBurned()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalMinutes: StateFlow<Int> = repository.getTotalWorkoutMinutes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCompletedWorkouts: StateFlow<Int> = repository.getTotalCompletedWorkouts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setCategoryFilter(category: WorkoutCategory?) {
        _selectedCategoryFilter.value = category
    }

    fun toggleWorkoutCompletion(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.toggleWorkoutCompletion(workout)
        }
    }

    fun toggleExerciseCompletion(exercise: WorkoutExerciseEntity) {
        viewModelScope.launch {
            repository.toggleExerciseCompletion(exercise)
        }
    }

    fun addWorkout(
        title: String,
        category: WorkoutCategory,
        durationMinutes: Int,
        caloriesBurned: Int,
        notes: String,
        exercises: List<WorkoutExerciseEntity>
    ) {
        viewModelScope.launch {
            val workout = WorkoutEntity(
                title = title,
                category = category,
                durationMinutes = durationMinutes,
                caloriesBurned = caloriesBurned,
                date = _selectedDate.value.toString(),
                notes = notes,
                isCompleted = false
            )
            repository.addWorkoutWithExercises(workout, exercises)
        }
    }

    fun updateWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.updateWorkout(workout)
        }
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }
}

class WorkoutViewModelFactory(
    private val repository: WorkoutRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
