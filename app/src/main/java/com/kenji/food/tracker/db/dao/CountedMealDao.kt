package com.kenji.food.tracker.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.FoodPerDay
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface CountedMealDao {
    @Insert
    suspend fun insert(countedMealEntity: CountedMealEntity): Long

    @Query("UPDATE food SET lastUsed = :lastUsed WHERE id = :id")
    suspend fun updateLastUsed(id: Int, lastUsed: Long = Instant.now().toEpochMilli())

    @Transaction
    suspend fun insertAndUpdate(countedMealEntity: CountedMealEntity, foodId: Int) {
        insert(countedMealEntity)

        updateLastUsed(foodId)
    }

    @Transaction
    @Query(
        """
        SELECT * FROM counted_meal
        ORDER BY counted_meal.eatenAt DESC,
        counted_meal.name
        """
    )
    fun getAll(): PagingSource<Int, CountedMealEntity>

    @Query("DELETE FROM counted_meal WHERE counted_meal.id IN (:ids)")
    suspend fun delete(ids: Set<Int>)

    @Query(
        """
        SELECT * FROM counted_meal
        WHERE DATE(DATETIME(`eatenAt` / 1000, 'unixepoch')) = DATE('now')
        """
    )
    fun getToday(): Flow<List<CountedMealEntity>>

    @Query(
        """
        SELECT (CAST(strftime('%w', DATE(eatenAt / 1000, 'unixepoch')) AS INTEGER) + 6) % 7 AS day, SUM(calories) AS calories, SUM(protein) AS protein, SUM(sugar) as sugar
        FROM counted_meal
        WHERE DATE(eatenAt / 1000, 'unixepoch') > DATE(strftime('%s', 'now', '-7 days'), 'unixepoch')
        AND (CAST(strftime('%w', DATE(eatenAt / 1000, 'unixepoch')) AS INTEGER) + 6) % 7 <= (CAST(strftime('%w', DATE(strftime('%s', 'now', '-7 days'), 'unixepoch')) AS INTEGER) + 6) % 7
        GROUP BY strftime("%w", DATE(eatenAt / 1000, 'unixepoch'))
    """
    )
    fun getCaloriesPerDayThisWeek(): Flow<List<FoodPerDay>>
}
