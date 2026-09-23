package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.repository.HabitRepository
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.HabitViewModel
import com.example.myapplication.ui.viewmodel.HabitViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    private val repository by lazy { HabitRepository(database.habitDao()) }
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen(viewModel = habitViewModel)
            }
        }
    }
}
