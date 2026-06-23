package com.kenji.food.tracker.ui.viewmodel.count

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kenji.food.tracker.db.dao.CountedMealDao
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountViewModel @Inject constructor(
    foodDao: FoodDao,
    private val countedMealDao: CountedMealDao
) : ViewModel() {
    private val _state = MutableStateFlow(CountState())
    val state = _state.asStateFlow()

    val meals = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { foodDao.getAll() }
    ).flow.cachedIn(viewModelScope)

    fun onAction(action: CountAction) {
        when (action) {
            CountAction.ToggleSelectMode -> this.onToggleSelectMode()
            is CountAction.SelectMeal -> this.onSelectMeal(action.meal)
            CountAction.CountMeal -> this.onCountMeal()
        }
    }

    private fun onToggleSelectMode() {
        _state.update { it.copy(isSelectMode = !it.isSelectMode) }
    }

    private fun onSelectMeal(meal: Recipe) {
        _state.update { it.copy(selectedMeal = meal, isSelectMode = false) }
    }

    private fun onCountMeal() {
        val selectedMeal = state.value.selectedMeal ?: return

        val countedMeal = if (selectedMeal.food.isRecipe) {
            val initialValue = CountedMealEntity(
                id = 0,
                name = selectedMeal.food.name,
                calories = 0,
                carbs = 0,
                sugar = 0,
                protein = 0,
                fats = 0,
            )

            selectedMeal.foods.fold(initialValue) { acc, entity ->
                return@fold acc.copy(
                    calories = acc.calories?.plus((entity.calories ?: 0)),
                    carbs = acc.carbs?.plus((entity.carbs ?: 0)),
                    sugar = acc.sugar?.plus((entity.sugar ?: 0)),
                    protein = acc.protein?.plus((entity.protein ?: 0)),
                    fats = acc.fats?.plus((entity.fats ?: 0)),
                )
            }
        } else {
            CountedMealEntity(
                id = 0,
                name = selectedMeal.food.name,
                calories = selectedMeal.food.calories,
                carbs = selectedMeal.food.carbs,
                sugar = selectedMeal.food.sugar,
                protein = selectedMeal.food.protein,
                fats = selectedMeal.food.fats,
            )
        }

        viewModelScope.launch {
            countedMealDao.insert(countedMeal)
        }
    }
}