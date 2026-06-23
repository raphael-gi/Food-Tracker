package com.kenji.food.tracker.ui.viewmodel.onboarding.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.ProfileDao
import com.kenji.food.tracker.entity.FoodTargetEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingInputViewModel @Inject constructor(private val profileDao: ProfileDao) :
    ViewModel() {
    private val _state = MutableStateFlow(OnboardingInputState())
    val state = _state.asStateFlow()

    private val _effect = Channel<OnboardingInputEffect>()
    val effect = _effect.receiveAsFlow()

    fun onAction(action: OnboardingInputAction) {
        when (action) {
            is OnboardingInputAction.SetCalorieTarget -> this.onSetCalories(action.input)
            is OnboardingInputAction.SetProteinTarget -> this.onSetProtein(action.input)
            is OnboardingInputAction.SetSugarTarget -> this.onSetSugar(action.input)
            OnboardingInputAction.Finish -> this.onFinish()
        }
    }

    private fun onSetCalories(input: String) {
        val calories = input.toIntOrNull()
        _state.update { it.copy(calorieTarget = calories) }
    }

    private fun onSetProtein(input: String) {
        val protein = input.toIntOrNull()
        _state.update { it.copy(proteinTarget = protein) }
    }

    private fun onSetSugar(input: String) {
        val sugar = input.toIntOrNull()
        _state.update { it.copy(sugarTarget = sugar) }
    }

    private fun onFinish() {
        val calorieTarget = state.value.calorieTarget ?: return

        val foodTarget = FoodTargetEntity(
            id = 0,
            calories = calorieTarget,
            protein = state.value.proteinTarget,
            sugar = state.value.sugarTarget
        )

        viewModelScope.launch {
            profileDao.insertFoodTarget(foodTarget)

            _effect.send(OnboardingInputEffect.FinishOnboardingInput)
        }
    }
}