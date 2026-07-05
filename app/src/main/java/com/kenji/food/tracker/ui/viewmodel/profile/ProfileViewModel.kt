package com.kenji.food.tracker.ui.viewmodel.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.ProfileDao
import com.kenji.food.tracker.util.Permissions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDao: ProfileDao,
    @param:ApplicationContext private val applicationContext: Context
) : ViewModel() {
    private val _state = MutableStateFlow(
        ProfileState(
            barcodeScanningEnabled = Permissions.hasBarcodePermissions(applicationContext)
        )
    )
    val state = _state.asStateFlow()

    private val _effect = Channel<ProfileEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        this.loadCurrentTarget()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.UpdateFoodTarget -> this.onUpdateFoodTarget()
            ProfileAction.ToggleBarcodePermission -> this.onToggleBarcodePermission()
        }
    }

    private fun onUpdateFoodTarget() {
        val currentFoodTarget = state.value.currentFoodTarget ?: return

        viewModelScope.launch {
            _effect.send(ProfileEffect.OnUpdateFoodTarget(currentFoodTarget))
        }
    }

    private fun onToggleBarcodePermission() {
        _state.update {
            it.copy(
                barcodeScanningEnabled = Permissions.hasBarcodePermissions(
                    applicationContext
                )
            )
        }
    }

    private fun loadCurrentTarget() {
        viewModelScope.launch {
            profileDao.getCurrentTarget().collect { foodTarget ->
                _state.update { it.copy(currentFoodTarget = foodTarget) }
            }
        }
    }
}