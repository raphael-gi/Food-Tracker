package com.kenji.food.tracker.ui.viewmodel.recipe.modify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.entity.RecipeFoodEntity
import com.kenji.food.tracker.util.ModifyMode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = ModifyRecipeViewModel.Factory::class)
class ModifyRecipeViewModel @AssistedInject constructor(
    private val foodDao: FoodDao,
    @Assisted private val mode: ModifyMode
) : ViewModel() {
    private val _state = MutableStateFlow(ModifyRecipeState(mode = mode))
    val state = _state.asStateFlow()

    val foods = state
        .map { it.query }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(
                config = PagingConfig(pageSize = 5),
                pagingSourceFactory = { foodDao.getAllFoods("%$query%") }
            ).flow
        }.cachedIn(viewModelScope)

    private val _effect = Channel<ModifyRecipeEffect>()
    val effect = _effect.receiveAsFlow()

    @AssistedFactory
    interface Factory {
        fun create(mode: ModifyMode): ModifyRecipeViewModel
    }

    init {
        if (mode is ModifyMode.Edit) {
            viewModelScope.launch {
                foodDao.getRecipeById(mode.id).collect { recipe ->
                    _state.update {
                        it.copy(
                            name = recipe.food.name,
                            portions = recipe.food.quantity,
                            selectedFoods = recipe.foods.associateBy { food -> food.foodId },
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: ModifyRecipeAction) {
        when (action) {
            is ModifyRecipeAction.SetName -> this.onSetName(action.name)
            ModifyRecipeAction.ToggleSelectMode -> this.onToggleSelectMode()
            ModifyRecipeAction.LaunchCamera -> this.onLaunchCamera()
            is ModifyRecipeAction.CodeScanned -> this.onCodeScanned(action.code)
            is ModifyRecipeAction.ToggleSelection -> this.onToggleSelection(action.food)
            is ModifyRecipeAction.SetRecipeFoodQuantity -> this.onSetRecipeQuantity(
                action.food,
                action.input
            )

            is ModifyRecipeAction.Search -> this.onSearch(action.query)
            is ModifyRecipeAction.SetPortions -> this.onSetPortions(action.input)
            ModifyRecipeAction.Create -> this.onCreate()
        }
    }

    private fun onSetName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun onToggleSelectMode() {
        _state.update { it.copy(isSelectMode = !it.isSelectMode) }
    }

    private fun onLaunchCamera() {
        viewModelScope.launch {
            _effect.send(ModifyRecipeEffect.LaunchCamera)
        }
    }

    private fun onCodeScanned(code: String) {
        viewModelScope.launch {
            val scannedFood = foodDao.getFoodByCode(code)

            when (scannedFood.size) {
                0 -> _effect.send(ModifyRecipeEffect.ScanNotFound)
                1 -> {
                    val food = scannedFood.first()

                    if (state.value.selectedFoods.contains(food.id)) {
                        return@launch
                    }

                    onToggleSelection(food)
                }

                else -> {}
            }
        }
    }

    private fun onToggleSelection(food: FoodEntity) {
        _state.update {
            val newFoods = if (it.selectedFoods.contains(food.id)) {
                it.selectedFoods - food.id
            } else {
                val recipeFood = RecipeFoodEntity(
                    recipeId = 0,
                    foodId = food.id,
                    food = food,
                    recipeQuantity = food.quantity
                )

                it.selectedFoods + (food.id to recipeFood)
            }

            it.copy(selectedFoods = newFoods)
        }
    }

    private fun onSetRecipeQuantity(food: FoodEntity, input: String) {
        val recipeQuantity = input.toDoubleOrNull() ?: return

        _state.update {
            val newRecipeFoods = it.selectedFoods.mapValues { entry ->
                if (entry.key == food.id) {
                    RecipeFoodEntity(
                        recipeId = 0,
                        foodId = food.id,
                        food = food,
                        recipeQuantity = recipeQuantity
                    )
                } else entry.value
            }

            it.copy(selectedFoods = newRecipeFoods)
        }
    }

    private fun onSearch(query: String) {
        _state.update { it.copy(query = query) }
    }

    private fun onSetPortions(input: String) {
        val portions = input.toDoubleOrNull()
        _state.update { it.copy(portions = portions) }
    }

    private fun onCreate() {
        val name = state.value.name.ifEmpty { return }
        val portion = state.value.portions ?: return

        val id = when (mode) {
            is ModifyMode.Edit -> mode.id
            is ModifyMode.Create -> 0
        }

        val entity = FoodEntity(
            id = id,
            name = name,
            calories = null,
            protein = null,
            quantity = portion,
            isRecipe = true,
            unit = FoodUnit.PC,
        )

        val recipe = Recipe(
            food = entity,
            foods = state.value.selectedFoods.values.toList()
        )

        viewModelScope.launch {
            foodDao.upsertRecipe(recipe)
            _effect.send(ModifyRecipeEffect.NavBack)
        }
    }
}