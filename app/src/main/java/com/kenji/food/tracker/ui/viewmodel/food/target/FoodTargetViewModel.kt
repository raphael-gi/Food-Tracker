package com.kenji.food.tracker.ui.viewmodel.food.target

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.ProfileDao
import com.kenji.food.tracker.entity.FoodTargetEntity
import com.kenji.food.tracker.ui.Route
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

@HiltViewModel(assistedFactory = FoodTargetViewModel.Factory::class)
class FoodTargetViewModel @AssistedInject constructor(
    private val profileDao: ProfileDao,
    @Assisted private val route: Route.FoodTarget
) : ViewModel() {
    private val _state = MutableStateFlow(
        FoodTargetState(
            calories = route.calories,
            proteins = route.proteins,
            sugar = route.sugar
        )
    )
    val state = _state.asStateFlow()

    private val _effect = Channel<FoodTargetEffect>()
    val effect = _effect.receiveAsFlow()

    @AssistedFactory
    interface Factory {
        fun create(navKey: Route.FoodTarget): FoodTargetViewModel
    }

    fun onAction(action: FoodTargetAction) {
        when (action) {
            is FoodTargetAction.SetCalories -> this.onSetCalories(action.input)
            is FoodTargetAction.SetProteins -> this.onSetProtein(action.input)
            is FoodTargetAction.SetSugar -> this.onSetSugar(action.input)
            FoodTargetAction.Update -> this.onUpdate()
        }
    }

    private fun onSetCalories(input: String) {
        val calories = input.toIntOrNull() ?: return
        _state.update { it.copy(calories = calories) }
    }

    private fun onSetProtein(input: String) {
        val protein = input.toIntOrNull()
        _state.update { it.copy(proteins = protein) }
    }

    private fun onSetSugar(input: String) {
        val sugar = input.toIntOrNull()
        _state.update { it.copy(sugar = sugar) }
    }

    private fun onUpdate() {
        val calories = state.value.calories
        val proteins = state.value.proteins
        val sugar = state.value.sugar

        if (route.calories == calories && route.proteins == proteins && route.sugar == sugar) {
            return
        }

        val entity = FoodTargetEntity(
            id = 0,
            calories = calories,
            protein = proteins,
            sugar = sugar,
        )

        viewModelScope.launch {
            profileDao.insertFoodTarget(entity)
            _effect.send(FoodTargetEffect.NavBack)
        }
    }
}