package com.kenji.food.tracker.di

import android.content.Context
import com.kenji.food.tracker.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFoodDao(database: AppDatabase) = database.foodDao()

    @Provides
    @Singleton
    fun provideCountedMealDao(database: AppDatabase) = database.countedMealDao()

    @Provides
    @Singleton
    fun provideProfileDao(database: AppDatabase) = database.profileDao()
}