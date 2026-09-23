package com.example.myapplication.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.model.FrequencyType
import com.example.myapplication.data.model.HabitCategory
import com.example.myapplication.data.model.WorkoutCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Database(
    entities = [
        HabitEntity::class,
        HabitLogEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                lateinit var databaseRef: AppDatabase
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habit_tracker_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            scope.launch(Dispatchers.IO) {
                                populateInitialData(databaseRef.habitDao(), databaseRef.workoutDao())
                            }
                        }
                    })
                    .build()
                databaseRef = instance
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(habitDao: HabitDao, workoutDao: WorkoutDao) {
            if (habitDao.getHabitsCount() == 0) {
                val initialHabits = listOf(
                    HabitEntity(
                        name = "Drink Water",
                        description = "Stay hydrated throughout the day",
                        category = HabitCategory.HEALTH,
                        targetCount = 8,
                        unit = "glasses",
                        frequency = FrequencyType.DAILY,
                        colorHex = 0xFF2196F3,
                        iconName = "LocalWater"
                    ),
                    HabitEntity(
                        name = "Morning Workout",
                        description = "30 minutes of cardio or strength exercises",
                        category = HabitCategory.FITNESS,
                        targetCount = 1,
                        unit = "workout",
                        frequency = FrequencyType.DAILY,
                        colorHex = 0xFFFF5722,
                        iconName = "FitnessCenter"
                    ),
                    HabitEntity(
                        name = "Read Books",
                        description = "Read non-fiction or educational material",
                        category = HabitCategory.LEARNING,
                        targetCount = 15,
                        unit = "pages",
                        frequency = FrequencyType.DAILY,
                        colorHex = 0xFFFF9800,
                        iconName = "Book"
                    ),
                    HabitEntity(
                        name = "Meditation",
                        description = "Mindful breathing and silence",
                        category = HabitCategory.MINDFULNESS,
                        targetCount = 10,
                        unit = "mins",
                        frequency = FrequencyType.DAILY,
                        colorHex = 0xFF9C27B0,
                        iconName = "SelfImprovement"
                    ),
                    HabitEntity(
                        name = "Code / Study",
                        description = "Practice Android coding & Kotlin",
                        category = HabitCategory.PRODUCTIVITY,
                        targetCount = 1,
                        unit = "session",
                        frequency = FrequencyType.WEEKDAYS,
                        colorHex = 0xFF4CAF50,
                        iconName = "Code"
                    )
                )

                initialHabits.forEach { habitDao.insertHabit(it) }
            }

            if (workoutDao.getWorkoutsCount() == 0) {
                val todayStr = LocalDate.now().toString()

                // Sample Workout 1: Strength Training
                val w1Id = workoutDao.insertWorkout(
                    WorkoutEntity(
                        title = "Upper Body Strength",
                        category = WorkoutCategory.STRENGTH,
                        durationMinutes = 45,
                        caloriesBurned = 350,
                        date = todayStr,
                        notes = "Pushed heavy sets today! Felt great.",
                        isCompleted = true
                    )
                )
                workoutDao.insertExercise(
                    WorkoutExerciseEntity(
                        workoutId = w1Id,
                        name = "Bench Press",
                        sets = 4,
                        repsOrDistance = "10 reps",
                        weightKg = 60.0,
                        isCompleted = true
                    )
                )
                workoutDao.insertExercise(
                    WorkoutExerciseEntity(
                        workoutId = w1Id,
                        name = "Incline Dumbbell Press",
                        sets = 3,
                        repsOrDistance = "12 reps",
                        weightKg = 22.0,
                        isCompleted = true
                    )
                )
                workoutDao.insertExercise(
                    WorkoutExerciseEntity(
                        workoutId = w1Id,
                        name = "Tricep Pushdowns",
                        sets = 3,
                        repsOrDistance = "15 reps",
                        weightKg = 25.0,
                        isCompleted = true
                    )
                )

                // Sample Workout 2: Cardio & Running
                val w2Id = workoutDao.insertWorkout(
                    WorkoutEntity(
                        title = "5K Morning Run",
                        category = WorkoutCategory.RUNNING,
                        durationMinutes = 30,
                        caloriesBurned = 280,
                        date = todayStr,
                        notes = "Maintained a steady 6 min/km pace.",
                        isCompleted = false
                    )
                )
                workoutDao.insertExercise(
                    WorkoutExerciseEntity(
                        workoutId = w2Id,
                        name = "Outdoor Run",
                        sets = 1,
                        repsOrDistance = "5.0 km",
                        isCompleted = false
                    )
                )

                // Sample Workout 3: HIIT Fat Burner
                val w3Id = workoutDao.insertWorkout(
                    WorkoutEntity(
                        title = "Core & HIIT Circuit",
                        category = WorkoutCategory.HIIT,
                        durationMinutes = 25,
                        caloriesBurned = 220,
                        date = todayStr,
                        notes = "40 secs on, 20 secs off.",
                        isCompleted = false
                    )
                )
                workoutDao.insertExercise(
                    WorkoutExerciseEntity(
                        workoutId = w3Id,
                        name = "Burpees",
                        sets = 4,
                        repsOrDistance = "45 sec",
                        isCompleted = false
                    )
                )
                workoutDao.insertExercise(
                    WorkoutExerciseEntity(
                        workoutId = w3Id,
                        name = "Plank Hold",
                        sets = 3,
                        repsOrDistance = "60 sec",
                        isCompleted = false
                    )
                )
            }
        }
    }
}
