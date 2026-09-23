package com.example.myapplication.data.db

import androidx.room.TypeConverter
import com.example.myapplication.data.model.FrequencyType
import com.example.myapplication.data.model.HabitCategory
import com.example.myapplication.data.model.MealType
import com.example.myapplication.data.model.WorkoutCategory

class Converters {
    @TypeConverter
    fun fromCategory(category: HabitCategory): String = category.name

    @TypeConverter
    fun toCategory(value: String): HabitCategory = try {
        HabitCategory.valueOf(value)
    } catch (e: Exception) {
        HabitCategory.HEALTH
    }

    @TypeConverter
    fun fromFrequency(frequency: FrequencyType): String = frequency.name

    @TypeConverter
    fun toFrequency(value: String): FrequencyType = try {
        FrequencyType.valueOf(value)
    } catch (e: Exception) {
        FrequencyType.DAILY
    }

    @TypeConverter
    fun fromWorkoutCategory(category: WorkoutCategory): String = category.name

    @TypeConverter
    fun toWorkoutCategory(value: String): WorkoutCategory = try {
        WorkoutCategory.valueOf(value)
    } catch (e: Exception) {
        WorkoutCategory.STRENGTH
    }

    @TypeConverter
    fun fromMealType(type: MealType): String = type.name

    @TypeConverter
    fun toMealType(value: String): MealType = try {
        MealType.valueOf(value)
    } catch (e: Exception) {
        MealType.BREAKFAST
    }
}
