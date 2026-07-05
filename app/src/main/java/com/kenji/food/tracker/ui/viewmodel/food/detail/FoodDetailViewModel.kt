package com.kenji.food.tracker.ui.viewmodel.food.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.ui.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = FoodDetailViewModel.Factory::class)
class FoodDetailViewModel @AssistedInject constructor(
    foodDao: FoodDao,
    @Assisted private val route: Route.FoodDetail
) : ViewModel() {
    private val _state = MutableStateFlow(FoodDetailState())
    val state = _state.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(navKey: Route.FoodDetail): FoodDetailViewModel
    }

    init {
        viewModelScope.launch {
            foodDao.getFoodById(route.id).collect { food ->
                _state.update { it.copy(food = food) }
            }
        }
    }
}