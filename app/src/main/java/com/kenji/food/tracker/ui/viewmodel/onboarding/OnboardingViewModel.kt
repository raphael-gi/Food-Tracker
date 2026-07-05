package com.kenji.food.tracker.ui.viewmodel.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.ProfileDao
import com.kenji.food.tracker.entity.FoodTargetEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val profileDao: ProfileDao) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val _effect = Channel<OnboardingEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            val currentFoodTarget = profileDao.getCurrentTarget().firstOrNull()
            if (currentFoodTarget != null) {
                _effect.send(OnboardingEffect.FinishOnboarding)
            } else {
                _state.update { it.copy(currentStep = OnboardingStep.CALORIE_INPUT) }
            }
        }
    }

    fun onAction(action: OnboardingAction) {
        when (action) {
            OnboardingAction.StepBack -> this.onStepBack()
            OnboardingAction.ConfirmCalories -> this.onConfirmCalories()
            is OnboardingAction.SetCalorieTarget -> this.onSetCalories(action.input)
            OnboardingAction.Finish -> this.onFinish()
        }
    }

    private fun onStepBack() {
        val currentScreenIndex = state.value.currentStep.ordinal
        val nextScreen = OnboardingStep.entries[currentScreenIndex - 1]

        _state.update { it.copy(currentStep = nextScreen) }
    }

    private fun onConfirmCalories() {
        if (state.value.calorieTarget == null) {
            return
        }

        _state.update { it.copy(currentStep = OnboardingStep.SCAN_PERMISSION) }
    }

    private fun onSetCalories(input: String) {
        val calories = input.toIntOrNull()
        _state.update { it.copy(calorieTarget = calories) }
    }

    private fun onFinish() {
        val calorieTarget = state.value.calorieTarget ?: return

        val foodTarget = FoodTargetEntity(
            id = 0,
            calories = calorieTarget,
            protein = null,
            sugar = null
        )

        viewModelScope.launch {
            profileDao.insertFoodTarget(foodTarget)

            _effect.send(OnboardingEffect.FinishOnboarding)
        }
    }
}