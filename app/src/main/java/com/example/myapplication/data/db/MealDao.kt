package com.example.myapplication.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Query("SELECT * FROM meals ORDER BY date DESC, id DESC")
    fun getAllMeals(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE date = :date ORDER BY id DESC")
    fun getMealsForDate(date: String): Flow<List<MealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("SELECT COUNT(*) FROM meals")
    suspend fun getMealsCount(): Int

    @Query("SELECT SUM(calories) FROM meals WHERE date = :date")
    fun getTotalCaloriesForDate(date: String): Flow<Int?>

    @Query("SELECT SUM(proteinGrams) FROM meals WHERE date = :date")
    fun getTotalProteinForDate(date: String): Flow<Double?>

    @Query("SELECT SUM(carbsGrams) FROM meals WHERE date = :date")
    fun getTotalCarbsForDate(date: String): Flow<Double?>

    @Query("SELECT SUM(fatGrams) FROM meals WHERE date = :date")
    fun getTotalFatForDate(date: String): Flow<Double?>
}
