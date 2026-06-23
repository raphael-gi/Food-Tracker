package com.kenji.food.tracker.ui.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.ProfileDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileDao: ProfileDao) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ProfileEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        this.loadCurrentState()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.UpdateFoodTarget -> this.onUpdateFoodTarget()
        }
    }

    private fun onUpdateFoodTarget() {
        val currentFoodTarget = state.value.currentFoodTarget ?: return

        viewModelScope.launch {
            _effect.send(ProfileEffect.OnUpdateFoodTarget(currentFoodTarget))
        }
    }


    private fun loadCurrentState() {
        viewModelScope.launch {
            profileDao.getCurrentTarget().collect { foodTarget ->
                _state.update { it.copy(currentFoodTarget = foodTarget) }
            }
        }
    }
}