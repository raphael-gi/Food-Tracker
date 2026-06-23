package com.kenji.food.tracker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kenji.food.tracker.entity.FoodTargetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Insert
    suspend fun insertFoodTarget(foodTargetEntity: FoodTargetEntity): Long

    @Query("SELECT * FROM food_target ORDER BY createdAt DESC LIMIT 1")
    fun getCurrentTarget(): Flow<FoodTargetEntity?>
}
