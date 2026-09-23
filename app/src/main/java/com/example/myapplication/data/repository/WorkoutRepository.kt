package com.example.myapplication.data.repository

import com.example.myapplication.data.db.WorkoutDao
import com.example.myapplication.data.db.WorkoutEntity
import com.example.myapplication.data.db.WorkoutExerciseEntity
import com.example.myapplication.data.db.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    fun getWorkoutsForDate(date: LocalDate): Flow<List<WorkoutWithExercises>> {
        return workoutDao.getWorkoutsForDateWithExercises(date.toString())
    }

    fun getAllWorkouts(): Flow<List<WorkoutWithExercises>> {
        return workoutDao.getAllWorkoutsWithExercises()
    }

    fun getTotalCaloriesBurned(): Flow<Int> {
        return workoutDao.getTotalCaloriesBurned().map { it ?: 0 }
    }

    fun getTotalWorkoutMinutes(): Flow<Int> {
        return workoutDao.getTotalWorkoutMinutes().map { it ?: 0 }
    }

    fun getTotalCompletedWorkouts(): Flow<Int> {
        return workoutDao.getTotalCompletedWorkouts().map { it ?: 0 }
    }

    suspend fun addWorkoutWithExercises(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>
    ) {
        val workoutId = workoutDao.insertWorkout(workout)
        exercises.forEach { exercise ->
            workoutDao.insertExercise(exercise.copy(workoutId = workoutId))
        }
    }

    suspend fun updateWorkout(workout: WorkoutEntity) {
        workoutDao.updateWorkout(workout)
    }

    suspend fun toggleWorkoutCompletion(workout: WorkoutEntity) {
        val updated = workout.copy(isCompleted = !workout.isCompleted)
        workoutDao.updateWorkout(updated)
    }

    suspend fun toggleExerciseCompletion(exercise: WorkoutExerciseEntity) {
        val updated = exercise.copy(isCompleted = !exercise.isCompleted)
        workoutDao.updateExercise(updated)
    }

    suspend fun deleteWorkout(workout: WorkoutEntity) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun addExercise(exercise: WorkoutExerciseEntity) {
        workoutDao.insertExercise(exercise)
    }

    suspend fun deleteExercise(exercise: WorkoutExerciseEntity) {
        workoutDao.deleteExercise(exercise)
    }
}
