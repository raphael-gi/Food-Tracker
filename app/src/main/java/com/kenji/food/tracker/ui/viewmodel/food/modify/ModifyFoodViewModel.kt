package com.kenji.food.tracker.ui.viewmodel.food.modify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.entity.FoodCategory
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.util.ModifyMode
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

@HiltViewModel(assistedFactory = ModifyFoodViewModel.Factory::class)
class ModifyFoodViewModel @AssistedInject constructor(
    private val foodDao: FoodDao,
    @Assisted private val mode: ModifyMode,
) : ViewModel() {
    private val _state = MutableStateFlow(ModifyFoodState(mode = mode))
    val state = _state.asStateFlow()

    private val _effect = Channel<ModifyFoodEffect>()
    val effect = _effect.receiveAsFlow()

    @AssistedFactory
    interface Factory {
        fun create(mode: ModifyMode): ModifyFoodViewModel
    }

    init {
        if (mode is ModifyMode.Edit) {
            _state.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                foodDao.getFoodById(mode.id).collect { food ->
                    _state.update {
                        it.copy(
                            name = food.name,
                            calories = food.calories,
                            fats = food.fats,
                            saturatedFats = food.saturatedFats,
                            carbs = food.carbs,
                            sugar = food.sugar,
                            proteins = food.protein,
                            unit = food.unit,
                            category = food.category,
                            code = food.code,
                            quantity = food.quantity,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: ModifyFoodAction) {
        when (action) {
            is ModifyFoodAction.SetName -> this.onSetName(action.name)
            is ModifyFoodAction.SetCalories -> this.onSetCalories(action.input)
            is ModifyFoodAction.SetFats -> this.onSetFats(action.input)
            is ModifyFoodAction.SetSaturatedFats -> this.onSetSaturatedFats(action.input)
            is ModifyFoodAction.SetCarbs -> this.onSetCarbs(action.input)
            is ModifyFoodAction.SetSugar -> this.onSetSugar(action.input)
            is ModifyFoodAction.SetProteins -> this.onSetProtein(action.input)
            is ModifyFoodAction.SetFoodUnit -> this.onSetFoodUnit(action.input)
            is ModifyFoodAction.SetFoodCategory -> this.onSetFoodCategory(action.input)
            is ModifyFoodAction.SetQuantity -> this.onSetQuantity(action.input)
            is ModifyFoodAction.SetCode -> this.onSetCode(action.code)
            ModifyFoodAction.RemoveCode -> this.onRemoveCode()
            ModifyFoodAction.Create -> this.onCreate()
        }
    }

    private fun onSetName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun onSetCalories(input: String) {
        val calories = input.toIntOrNull()
        _state.update { it.copy(calories = calories) }
    }

    private fun onSetFats(input: String) {
        val fats = input.toDoubleOrNull()
        _state.update { it.copy(fats = fats) }
    }

    private fun onSetSaturatedFats(input: String) {
        val saturatedFats = input.toDoubleOrNull()
        _state.update { it.copy(saturatedFats = saturatedFats) }
    }

    private fun onSetCarbs(input: String) {
        val carbs = input.toDoubleOrNull()
        _state.update { it.copy(carbs = carbs) }
    }

    private fun onSetSugar(input: String) {
        val sugar = input.toDoubleOrNull()
        _state.update { it.copy(sugar = sugar) }
    }

    private fun onSetProtein(input: String) {
        val protein = input.toDoubleOrNull()
        _state.update { it.copy(proteins = protein) }
    }

    private fun onSetFoodUnit(input: FoodUnit) {
        _state.update {
            val foodUnit = if (it.unit == input) null
            else input

            it.copy(unit = foodUnit)
        }
    }

    private fun onSetFoodCategory(input: FoodCategory) {
        _state.update {
            val foodCategory = if (it.category == input) null
            else input

            it.copy(category = foodCategory)
        }
    }

    private fun onSetQuantity(input: String) {
        val quantity = input.toDoubleOrNull()
        _state.update { it.copy(quantity = quantity) }
    }

    private fun onSetCode(code: String) {
        _state.update { it.copy(code = code) }
    }

    private fun onRemoveCode() {
        _state.update { it.copy(code = null) }
    }

    private fun onCreate() {
        val name = state.value.name
        val calories = state.value.calories ?: return
        val fats = state.value.fats
        val saturatedFats = state.value.saturatedFats
        val carbs = state.value.carbs
        val proteins = state.value.proteins
        val sugar = state.value.sugar
        val unit = state.value.unit ?: return
        val category = state.value.category
        val quantity = state.value.quantity ?: return
        val code = state.value.code

        val id = when (mode) {
            is ModifyMode.Edit -> mode.id
            is ModifyMode.Create -> 0
        }

        val entity = FoodEntity(
            name = name,
            id = id,
            calories = calories,
            fats = fats,
            saturatedFats = saturatedFats,
            carbs = carbs,
            sugar = sugar,
            protein = proteins,
            isRecipe = false,
            unit = unit,
            category = category,
            code = code,
            quantity = quantity,
        )

        viewModelScope.launch {
            foodDao.upsert(entity)
            _effect.send(ModifyFoodEffect.NavBack)
        }
    }
}