package com.kenji.food.tracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "counted_meal")
data class CountedMealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val calories: Int?,
    val carbs: Int? = null,
    val sugar: Int? = null,
    val protein: Int? = null,
    val fats: Int? = null,
    val eatenAt: Long = Instant.now().toEpochMilli()
)
