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
import com.kenji.food.tracker.util.CalorieCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountViewModel @Inject constructor(
    private val foodDao: FoodDao,
    private val countedMealDao: CountedMealDao,
) : ViewModel() {
    private val _state = MutableStateFlow(CountState())
    val state = _state.asStateFlow()

    private val _effect = Channel<CountEffect>()
    val effect = _effect.receiveAsFlow()

    val meals = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { foodDao.getAll() }
    ).flow.cachedIn(viewModelScope)

    fun onAction(action: CountAction) {
        when (action) {
            CountAction.LaunchCamera -> this.onLaunchCamera()
            is CountAction.CodeScanned -> this.onCodeScanned(action.code)
            CountAction.ToggleSelectMode -> this.onToggleSelectMode()
            is CountAction.SelectMeal -> this.onSelectMeal(action.meal)
            is CountAction.SetMealQuantity -> this.onSetMealQuantity(action.input)
            CountAction.CountMeal -> this.onCountMeal()
        }
    }

    private fun onLaunchCamera() {
        viewModelScope.launch {
            _effect.send(CountEffect.LaunchCamera)
        }
    }

    private fun onCodeScanned(code: String) {
        viewModelScope.launch {
            val scannedFood = foodDao.getFoodByCode(code)

            when (scannedFood.size) {
                0 -> _effect.send(CountEffect.ScanNotFound)
                1 -> onSelectMeal(
                    Recipe(
                        food = scannedFood.first(),
                        foods = emptyList()
                    )
                )

                else -> {}
            }
        }
    }

    private fun onToggleSelectMode() {
        _state.update { it.copy(isSelectMode = !it.isSelectMode) }
    }

    private fun onSelectMeal(meal: Recipe) {
        val quantity = if (meal.food.isRecipe) 1.0
        else meal.food.quantity

        _state.update {
            it.copy(
                selectedMeal = meal,
                countedMeal = getCountedMealOfRecipe(meal, quantity),
                quantity = quantity,
                isSelectMode = false
            )
        }
    }

    private fun onSetMealQuantity(input: String) {
        val quantity = input.toDoubleOrNull()

        _state.update {
            it.copy(
                quantity = quantity,
                countedMeal = it.selectedMeal?.let { recipe ->
                    getCountedMealOfRecipe(recipe, quantity ?: 0.0)
                }
            )
        }
    }

    private fun onCountMeal() {
        val countedMeal = state.value.countedMeal ?: return

        viewModelScope.launch {
            countedMealDao.insert(countedMeal)
            _effect.send(CountEffect.Finish)
        }
    }

    /**
     * Get a counted meal from a recipe
     *
     * @param recipe The recipe to calculate the stats from
     * @param quantity The quantity to multiply the meal by
     */
    private fun getCountedMealOfRecipe(recipe: Recipe, quantity: Double): CountedMealEntity {
        if (recipe.food.isRecipe) {
            return CalorieCalculator.calculateCountedRecipe(recipe, quantity)
        }

        return CalorieCalculator.calculateCountedFood(recipe.food, quantity)
    }
}