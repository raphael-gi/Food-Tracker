package com.kenji.food.tracker.widget.repository

import android.content.Context
import com.kenji.food.tracker.db.dao.CountedMealDao
import com.kenji.food.tracker.db.dao.ProfileDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetRepository @Inject constructor(profileDao: ProfileDao, countedMealDao: CountedMealDao) {
    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface WidgetRepositoryEntryPoint {
        fun widgetRepository(): WidgetRepository
    }

    companion object {
        fun get(context: Context): WidgetRepository {
            return EntryPointAccessors.fromApplication<WidgetRepositoryEntryPoint>(context)
                .widgetRepository()
        }
    }

    val state = combine(
        profileDao.getCurrentTarget(),
        countedMealDao.getToday()
    ) { currentTarget, today ->
        if (currentTarget == null) {
            WidgetUIState.NoTarget
        } else {
            WidgetState(
                target = currentTarget,
                caloriesToday = today.sumOf { it.calories ?: 0 },
                proteinsToday = today.sumOf { it.protein ?: 0.0 }
            )
        }
    }
}