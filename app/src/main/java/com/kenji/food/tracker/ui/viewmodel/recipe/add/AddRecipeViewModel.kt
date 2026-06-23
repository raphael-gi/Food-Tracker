package com.kenji.food.tracker.ui.viewmodel.recipe.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.entity.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {
    private val _state = MutableStateFlow(AddRecipeState())
    val state = _state.asStateFlow()

    val foods = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { foodDao.getAllFoods() }
    ).flow.cachedIn(viewModelScope)

    private val _effect = Channel<AddRecipeEffect>()
    val effect = _effect.receiveAsFlow()

    fun onAction(action: AddRecipeAction) {
        when (action) {
            is AddRecipeAction.SetName -> this.onSetName(action.name)
            AddRecipeAction.ToggleSelectMode -> this.onToggleSelectMode()
            is AddRecipeAction.ToggleSelection -> this.onToggleSelection(action.food)
            AddRecipeAction.Create -> this.onCreate()
        }
    }

    private fun onSetName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun onToggleSelectMode() {
        _state.update { it.copy(isSelectMode = !it.isSelectMode) }
    }

    private fun onToggleSelection(food: FoodEntity) {
        _state.update {
            val newFoods = if (it.selectedFoods.contains(food.id)) {
                it.selectedFoods - food.id
            } else {
                it.selectedFoods + (food.id to food)
            }

            it.copy(selectedFoods = newFoods)
        }
    }

    private fun onCreate() {
        val name = state.value.name

        val entity = FoodEntity(
            id = 0,
            name = name,
            calories = null,
            protein = null,
            quantity = 5,
            isRecipe = true,
            unit = FoodUnit.G,
        )

        val recipe = Recipe(
            food = entity,
            foods = state.value.selectedFoods.values.toList()
        )

        viewModelScope.launch {
            foodDao.insertTest(recipe)
            // foodDao.insertRecipe(entity, emptyList())
            _effect.send(AddRecipeEffect.NavBack)
        }
    }
}