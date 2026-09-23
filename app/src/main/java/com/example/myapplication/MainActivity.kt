package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.repository.HabitRepository
import com.example.myapplication.data.repository.MealRepository
import com.example.myapplication.data.repository.WorkoutRepository
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.HabitViewModel
import com.example.myapplication.ui.viewmodel.HabitViewModelFactory
import com.example.myapplication.ui.viewmodel.MealViewModel
import com.example.myapplication.ui.viewmodel.MealViewModelFactory
import com.example.myapplication.ui.viewmodel.ThemeViewModel
import com.example.myapplication.ui.viewmodel.ThemeViewModelFactory
import com.example.myapplication.ui.viewmodel.WorkoutViewModel
import com.example.myapplication.ui.viewmodel.WorkoutViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    private val habitRepository by lazy { HabitRepository(database.habitDao()) }
    private val workoutRepository by lazy { WorkoutRepository(database.workoutDao()) }
    private val mealRepository by lazy { MealRepository(database.mealDao()) }

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(habitRepository)
    }

    private val workoutViewModel: WorkoutViewModel by viewModels {
        WorkoutViewModelFactory(workoutRepository)
    }

    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory(mealRepository)
    }

    private val themeViewModel: ThemeViewModel by viewModels {
        ThemeViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentThemeMode by themeViewModel.themeMode.collectAsState()

            MyApplicationTheme(themeMode = currentThemeMode) {
                MainScreen(
                    habitViewModel = habitViewModel,
                    workoutViewModel = workoutViewModel,
                    mealViewModel = mealViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
