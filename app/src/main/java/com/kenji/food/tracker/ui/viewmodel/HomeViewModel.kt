package com.kenji.food.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.CountedMealDao
import com.kenji.food.tracker.db.dao.ProfileDao
import com.kenji.food.tracker.entity.FoodPerDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    countedMealDao: CountedMealDao,
    profileDao: ProfileDao
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                countedMealDao.getToday().collect { today ->
                    _state.update {
                        it.copy(
                            caloriesEatenToday = today.fold(0) { acc, entry ->
                                acc + (entry.calories ?: 0)
                            }
                        )
                    }
                }
            }

            launch {
                countedMealDao.getCaloriesPerDayThisWeek().collect { foodPerDay ->
                    val food = foodPerDay.toMutableList()
                    for (i in 0..6) {
                        if (food.getOrNull(i)?.day != i) {
                            food.add(i, FoodPerDay(i, 0, 0, 0))
                        }
                    }

                    _state.update {
                        it.copy(foodPerDays = food)
                    }
                }
            }

            launch {
                profileDao.getCurrentTarget().collect { currentTarget ->
                    _state.update { it.copy(currentTarget = currentTarget) }
                }
            }
        }
    }
}