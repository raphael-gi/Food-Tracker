package com.kenji.food.tracker.ui.viewmodel.onboarding.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.ProfileDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingLoadingViewModel @Inject constructor(profileDao: ProfileDao) : ViewModel() {
    private val _effect = Channel<OnboardingLoadingEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            val currentFoodTarget = profileDao.getCurrentTarget().firstOrNull()
            if (currentFoodTarget == null) {
                _effect.send(OnboardingLoadingEffect.ContinueOnboarding)
            } else {
                _effect.send(OnboardingLoadingEffect.SkipOnboarding)
            }
        }
    }
}