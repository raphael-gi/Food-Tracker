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
    private val countedMealDao: CountedMealDao,
    profileDao: ProfileDao
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                countedMealDao.getCaloriesPerDayThisWeek().collect { foodPerDay ->
                    val foods = Array(7) { FoodPerDay(0, it, 0, 0, 0) }
                    foodPerDay.forEach {
                        foods[it.dayOfWeek] = it
                    }

                    _state.update {
                        it.copy(foodPerDays = foods.toList())
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

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SelectDay -> this.onSelectDay(action.day)
            HomeAction.CloseOverlay -> this.onCloseOverlay()
        }
    }

    private fun onSelectDay(time: Long) {
        viewModelScope.launch {
            val day = time / 1000 / 60 / 60 / 24
            val countedMealsInDay = countedMealDao.getDay(day)
            _state.update { it.copy(selectedDay = countedMealsInDay) }
        }
    }

    private fun onCloseOverlay() {
        _state.update { it.copy(selectedDay = null) }
    }
}