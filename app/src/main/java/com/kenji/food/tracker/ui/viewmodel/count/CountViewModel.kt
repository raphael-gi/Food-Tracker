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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CountViewModel @Inject constructor(
    foodDao: FoodDao,
    private val countedMealDao: CountedMealDao
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
            CountAction.ToggleSelectMode -> this.onToggleSelectMode()
            is CountAction.SelectMeal -> this.onSelectMeal(action.meal)
            is CountAction.SetMealQuantity -> this.onSetMealQuantity(action.input)
            CountAction.CountMeal -> this.onCountMeal()
        }
    }

    private fun onToggleSelectMode() {
        _state.update { it.copy(isSelectMode = !it.isSelectMode) }
    }

    private fun onSelectMeal(meal: Recipe) {
        val quantity = meal.food.quantity

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
        val quantity = input.toIntOrNull() ?: return

        _state.update {
            it.copy(
                quantity = quantity,
                countedMeal = it.selectedMeal?.let { recipe ->
                    getCountedMealOfRecipe(
                        recipe,
                        quantity
                    )
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
     * @param quantity The quantity to multiply the food by
     */
    private fun getCountedMealOfRecipe(recipe: Recipe, quantity: Int): CountedMealEntity {
        if (!recipe.food.isRecipe) {
            val multiplier = quantity.toFloat() / recipe.food.quantity.toFloat()

            return CountedMealEntity(
                id = 0,
                name = recipe.food.name,
                calories = (recipe.food.calories?.times(multiplier))?.roundToInt(),
                carbs = recipe.food.carbs,
                sugar = recipe.food.sugar,
                protein = recipe.food.protein,
                fats = recipe.food.fats,
            )
        }

        val initialValue = CountedMealEntity(
            id = 0,
            name = recipe.food.name,
            calories = 0,
            carbs = 0,
            sugar = 0,
            protein = 0,
            fats = 0,
        )

        return recipe.foods.fold(initialValue) { acc, entity ->
            return@fold acc.copy(
                calories = acc.calories?.plus((entity.calories ?: 0)),
                carbs = acc.carbs?.plus((entity.carbs ?: 0)),
                sugar = acc.sugar?.plus((entity.sugar ?: 0)),
                protein = acc.protein?.plus((entity.protein ?: 0)),
                fats = acc.fats?.plus((entity.fats ?: 0)),
            )
        }
    }
}