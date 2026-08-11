package com.kenji.food.tracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "counted_meal")
data class CountedMealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val calories: Int?,
    val fats: Double? = null,
    val saturatedFats: Double? = null,
    val carbs: Double? = null,
    val sugar: Double? = null,
    val protein: Double? = null,
    val eatenAt: Long = Instant.now().toEpochMilli()
)
