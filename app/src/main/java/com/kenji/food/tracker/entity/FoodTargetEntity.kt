package com.kenji.food.tracker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "food_target")
data class FoodTargetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val calories: Int,
    val protein: Int?,
    val sugar: Int?,
    val createdAt: Long = Instant.now().toEpochMilli()
)