package com.kenji.food.tracker.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kenji.food.tracker.db.dao.CountedMealDao
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.db.dao.ProfileDao
import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodTargetEntity
import com.kenji.food.tracker.entity.RecipeFoodEntity

@Database(
    entities = [
        FoodEntity::class,
        RecipeFoodEntity::class,
        CountedMealEntity::class,
        FoodTargetEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun countedMealDao(): CountedMealDao
    abstract fun profileDao(): ProfileDao

    companion object {
        private const val DATABASE_NAME = "food-db"

        fun getDatabase(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}