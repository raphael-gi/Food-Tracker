package com.kenji.food.tracker.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.kenji.food.tracker.entity.CountedMealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountedMealDao {
    @Insert
    suspend fun insert(countedMealEntity: CountedMealEntity): Long

    @Transaction
    @Query(
        """
        SELECT * FROM counted_meal
        ORDER BY counted_meal.eatenAt DESC,
        counted_meal.name
        """
    )
    fun getAll(): PagingSource<Int, CountedMealEntity>

    @Query(
        """
        SELECT * FROM counted_meal
        WHERE DATE(DATETIME(`eatenAt` / 1000, 'unixepoch')) = DATE('now')
        """
    )
    fun getToday(): Flow<List<CountedMealEntity>>
}