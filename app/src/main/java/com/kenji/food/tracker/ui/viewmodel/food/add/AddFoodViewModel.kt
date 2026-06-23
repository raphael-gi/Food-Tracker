package com.kenji.food.tracker.ui.viewmodel.food.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {
    private val _state = MutableStateFlow(AddFoodState())
    val state = _state.asStateFlow()

    private val _effect = Channel<AddFoodEffect>()
    val effect = _effect.receiveAsFlow()

    fun onAction(action: AddFoodAction) {
        when (action) {
            is AddFoodAction.SetName -> this.onSetName(action.name)
            is AddFoodAction.SetCalories -> this.onSetCalories(action.input)
            is AddFoodAction.SetCarbs -> this.onSetCarbs(action.input)
            is AddFoodAction.SetProteins -> this.onSetProtein(action.input)
            is AddFoodAction.SetFats -> this.onSetFats(action.input)
            is AddFoodAction.SetFoodUnit -> this.onSetFoodUnit(action.input)
            is AddFoodAction.SetQuantity -> this.onSetQuantity(action.input)
            AddFoodAction.Create -> this.onCreate()
        }
    }

    private fun onSetName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun onSetCalories(input: String) {
        val calories = input.toIntOrNull()
        _state.update { it.copy(calories = calories) }
    }

    private fun onSetCarbs(input: String) {
        val carbs = input.toIntOrNull()
        _state.update { it.copy(carbs = carbs) }
    }

    private fun onSetProtein(input: String) {
        val protein = input.toIntOrNull()
        _state.update { it.copy(proteins = protein) }
    }

    private fun onSetFats(input: String) {
        val fats = input.toIntOrNull()
        _state.update { it.copy(fats = fats) }
    }

    private fun onSetFoodUnit(input: FoodUnit) {
        _state.update {
            val foodUnit = if (it.unit == input) null
            else input

            it.copy(unit = foodUnit)
        }
    }

    private fun onSetQuantity(input: String) {
        val quantity = input.toIntOrNull()
        _state.update { it.copy(quantity = quantity) }
    }

    private fun onCreate() {
        val name = state.value.name
        val calories = state.value.calories ?: return
        val carbs = state.value.carbs
        val proteins = state.value.proteins
        val fats = state.value.fats
        val unit = state.value.unit ?: return
        val quantity = state.value.quantity ?: return

        val entity = FoodEntity(
            name = name,
            id = 0,
            calories = calories,
            carbs = carbs,
            protein = proteins,
            fats = fats,
            isRecipe = false,
            unit = unit,
            quantity = quantity,
        )

        viewModelScope.launch {
            foodDao.insert(entity)
            _effect.send(AddFoodEffect.NavBack)
        }
    }
}