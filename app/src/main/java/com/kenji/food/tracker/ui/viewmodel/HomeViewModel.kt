package com.kenji.food.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.CountedMealDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(countedMealDao: CountedMealDao) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
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
    }
}