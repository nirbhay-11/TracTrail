package com.example.myapplication.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithLogs(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habitId"
    )
    val logs: List<HabitLogEntity>
)
