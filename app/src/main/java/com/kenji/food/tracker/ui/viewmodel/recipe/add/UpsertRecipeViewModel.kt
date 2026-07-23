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
import com.kenji.food.tracker.entity.RecipeFoodEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UpsertRecipeViewModel.Factory::class)
class UpsertRecipeViewModel @AssistedInject constructor(
    private val foodDao: FoodDao,
    @Assisted private val id: Int?
) : ViewModel() {
    private val _state = MutableStateFlow(
        UpsertRecipeState(
            isCreate = id == null,
            isLoading = id != null
        )
    )
    val state = _state.asStateFlow()

    val foods = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { foodDao.getAllFoods("%") }
    ).flow.cachedIn(viewModelScope)

    private val _effect = Channel<UpsertRecipeEffect>()
    val effect = _effect.receiveAsFlow()

    @AssistedFactory
    interface Factory {
        fun create(id: Int? = null): UpsertRecipeViewModel
    }

    init {
        if (id != null) {
            viewModelScope.launch {
                foodDao.getRecipeById(id).collect { recipe ->
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

    fun onAction(action: UpsertRecipeAction) {
        when (action) {
            is UpsertRecipeAction.SetName -> this.onSetName(action.name)
            UpsertRecipeAction.ToggleSelectMode -> this.onToggleSelectMode()
            UpsertRecipeAction.LaunchCamera -> this.onLaunchCamera()
            is UpsertRecipeAction.CodeScanned -> this.onCodeScanned(action.code)
            is UpsertRecipeAction.ToggleSelection -> this.onToggleSelection(action.food)
            is UpsertRecipeAction.SetRecipeFoodQuantity -> this.onSetRecipeQuantity(
                action.food,
                action.input
            )

            is UpsertRecipeAction.SetPortions -> this.onSetPortions(action.input)

            UpsertRecipeAction.Create -> this.onCreate()
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
            _effect.send(UpsertRecipeEffect.LaunchCamera)
        }
    }

    private fun onCodeScanned(code: String) {
        viewModelScope.launch {
            val scannedFood = foodDao.getFoodByCode(code)

            when (scannedFood.size) {
                0 -> _effect.send(UpsertRecipeEffect.ScanNotFound)
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

    private fun onSetPortions(input: String) {
        val portions = input.toDoubleOrNull()
        _state.update { it.copy(portions = portions) }
    }

    private fun onCreate() {
        val name = state.value.name.ifEmpty { return }
        val portion = state.value.portions ?: return

        val entity = FoodEntity(
            id = id ?: 0,
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
            _effect.send(UpsertRecipeEffect.NavBack)
        }
    }
}