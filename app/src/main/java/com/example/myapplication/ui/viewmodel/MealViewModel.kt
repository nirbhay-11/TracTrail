package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.db.MealEntity
import com.example.myapplication.data.model.MealType
import com.example.myapplication.data.repository.MealRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class NutritionGoals(
    val calorieGoal: Int = 2000,
    val proteinGoalGrams: Double = 140.0,
    val carbsGoalGrams: Double = 220.0,
    val fatGoalGrams: Double = 65.0
)

class MealViewModel(
    private val repository: MealRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _selectedMealTypeFilter = MutableStateFlow<MealType?>(null)
    val selectedMealTypeFilter: StateFlow<MealType?> = _selectedMealTypeFilter.asStateFlow()

    val goals = MutableStateFlow(NutritionGoals())

    @OptIn(ExperimentalCoroutinesApi::class)
    val meals: StateFlow<List<MealEntity>> = _selectedDate
        .flatMapLatest { date ->
            repository.getMealsForDate(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalCalories: StateFlow<Int> = _selectedDate
        .flatMapLatest { date -> repository.getTotalCaloriesForDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalProtein: StateFlow<Double> = _selectedDate
        .flatMapLatest { date -> repository.getTotalProteinForDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalCarbs: StateFlow<Double> = _selectedDate
        .flatMapLatest { date -> repository.getTotalCarbsForDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalFat: StateFlow<Double> = _selectedDate
        .flatMapLatest { date -> repository.getTotalFatForDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setMealTypeFilter(type: MealType?) {
        _selectedMealTypeFilter.value = type
    }

    fun addMeal(
        name: String,
        mealType: MealType,
        calories: Int,
        proteinGrams: Double,
        carbsGrams: Double,
        fatGrams: Double,
        time: String,
        notes: String
    ) {
        viewModelScope.launch {
            val meal = MealEntity(
                name = name,
                mealType = mealType,
                calories = calories,
                proteinGrams = proteinGrams,
                carbsGrams = carbsGrams,
                fatGrams = fatGrams,
                date = _selectedDate.value.toString(),
                time = time,
                notes = notes
            )
            repository.addMeal(meal)
        }
    }

    fun updateMeal(meal: MealEntity) {
        viewModelScope.launch {
            repository.updateMeal(meal)
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
    }
}

class MealViewModelFactory(
    private val repository: MealRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            return MealViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
