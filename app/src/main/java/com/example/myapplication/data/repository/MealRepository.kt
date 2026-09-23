package com.example.myapplication.data.repository

import com.example.myapplication.data.db.MealDao
import com.example.myapplication.data.db.MealEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class MealRepository(private val mealDao: MealDao) {

    fun getMealsForDate(date: LocalDate): Flow<List<MealEntity>> {
        return mealDao.getMealsForDate(date.toString())
    }

    fun getAllMeals(): Flow<List<MealEntity>> {
        return mealDao.getAllMeals()
    }

    fun getTotalCaloriesForDate(date: LocalDate): Flow<Int> {
        return mealDao.getTotalCaloriesForDate(date.toString()).map { it ?: 0 }
    }

    fun getTotalProteinForDate(date: LocalDate): Flow<Double> {
        return mealDao.getTotalProteinForDate(date.toString()).map { it ?: 0.0 }
    }

    fun getTotalCarbsForDate(date: LocalDate): Flow<Double> {
        return mealDao.getTotalCarbsForDate(date.toString()).map { it ?: 0.0 }
    }

    fun getTotalFatForDate(date: LocalDate): Flow<Double> {
        return mealDao.getTotalFatForDate(date.toString()).map { it ?: 0.0 }
    }

    suspend fun addMeal(meal: MealEntity): Long {
        return mealDao.insertMeal(meal)
    }

    suspend fun updateMeal(meal: MealEntity) {
        mealDao.updateMeal(meal)
    }

    suspend fun deleteMeal(meal: MealEntity) {
        mealDao.deleteMeal(meal)
    }
}
