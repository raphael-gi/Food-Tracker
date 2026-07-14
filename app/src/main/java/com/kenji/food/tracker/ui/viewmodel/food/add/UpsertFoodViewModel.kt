package com.kenji.food.tracker.ui.viewmodel.food.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UpsertFoodViewModel.Factory::class)
class UpsertFoodViewModel @AssistedInject constructor(
    private val foodDao: FoodDao,
    @Assisted private val id: Int?,
) : ViewModel() {
    private val _state = MutableStateFlow(
        UpsertFoodState(
            isCreate = id == null,
            isLoading = id != null
        )
    )
    val state = _state.asStateFlow()

    private val _effect = Channel<UpsertFoodEffect>()
    val effect = _effect.receiveAsFlow()

    @AssistedFactory
    interface Factory {
        fun create(id: Int? = null): UpsertFoodViewModel
    }

    init {
        if (id != null) {
            viewModelScope.launch {
                val food = foodDao.getFoodById(id).first()
                _state.update {
                    it.copy(
                        name = food.name,
                        calories = food.calories,
                        carbs = food.carbs,
                        proteins = food.protein,
                        fats = food.fats,
                        unit = food.unit,
                        quantity = food.quantity,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onAction(action: UpsertFoodAction) {
        when (action) {
            is UpsertFoodAction.SetName -> this.onSetName(action.name)
            is UpsertFoodAction.SetCalories -> this.onSetCalories(action.input)
            is UpsertFoodAction.SetCarbs -> this.onSetCarbs(action.input)
            is UpsertFoodAction.SetProteins -> this.onSetProtein(action.input)
            is UpsertFoodAction.SetFats -> this.onSetFats(action.input)
            is UpsertFoodAction.SetSugar -> this.onSetSugar(action.input)
            is UpsertFoodAction.SetFoodUnit -> this.onSetFoodUnit(action.input)
            is UpsertFoodAction.SetQuantity -> this.onSetQuantity(action.input)
            is UpsertFoodAction.SetCode -> this.onSetCode(action.code)
            UpsertFoodAction.RemoveCode -> this.onRemoveCode()
            UpsertFoodAction.Create -> this.onCreate()
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
        val carbs = input.toDoubleOrNull()
        _state.update { it.copy(carbs = carbs) }
    }

    private fun onSetProtein(input: String) {
        val protein = input.toDoubleOrNull()
        _state.update { it.copy(proteins = protein) }
    }

    private fun onSetFats(input: String) {
        val fats = input.toDoubleOrNull()
        _state.update { it.copy(fats = fats) }
    }

    private fun onSetSugar(input: String) {
        val sugar = input.toDoubleOrNull()
        _state.update { it.copy(sugar = sugar) }
    }

    private fun onSetFoodUnit(input: FoodUnit) {
        _state.update {
            val foodUnit = if (it.unit == input) null
            else input

            it.copy(unit = foodUnit)
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
        val carbs = state.value.carbs
        val proteins = state.value.proteins
        val fats = state.value.fats
        val sugar = state.value.sugar
        val unit = state.value.unit ?: return
        val quantity = state.value.quantity ?: return
        val code = state.value.code

        val entity = FoodEntity(
            name = name,
            id = id ?: 0,
            calories = calories,
            carbs = carbs,
            protein = proteins,
            fats = fats,
            sugar = sugar,
            isRecipe = false,
            unit = unit,
            code = code,
            quantity = quantity,
        )

        viewModelScope.launch {
            foodDao.upsert(entity)
            _effect.send(UpsertFoodEffect.NavBack)
        }
    }
}