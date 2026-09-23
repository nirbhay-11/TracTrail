package com.example.myapplication.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Transaction
    @Query("SELECT * FROM workouts ORDER BY date DESC, id DESC")
    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE date = :date ORDER BY id DESC")
    fun getWorkoutsForDateWithExercises(date: String): Flow<List<WorkoutWithExercises>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: WorkoutExerciseEntity): Long

    @Update
    suspend fun updateExercise(exercise: WorkoutExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: WorkoutExerciseEntity)

    @Query("SELECT COUNT(*) FROM workouts")
    suspend fun getWorkoutsCount(): Int

    @Query("SELECT SUM(caloriesBurned) FROM workouts WHERE isCompleted = 1")
    fun getTotalCaloriesBurned(): Flow<Int?>

    @Query("SELECT SUM(durationMinutes) FROM workouts WHERE isCompleted = 1")
    fun getTotalWorkoutMinutes(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM workouts WHERE isCompleted = 1")
    fun getTotalCompletedWorkouts(): Flow<Int?>
}
