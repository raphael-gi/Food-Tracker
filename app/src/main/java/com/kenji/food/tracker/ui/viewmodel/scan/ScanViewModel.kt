package com.kenji.food.tracker.ui.viewmodel.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
@HiltViewModel
class ScanViewModel @Inject constructor() : ViewModel() {
    companion object {
        private const val REQUIRED_SCANS = 15
    }

    private val _effect = Channel<ScanEffect>()
    val effect = _effect.receiveAsFlow()

    private val scans = hashMapOf<String, Int>()

    private val found = AtomicBoolean(false)

    fun onAction(action: ScanAction) {
        when (action) {
            is ScanAction.BarcodeScanned -> onBarcodeScanned(action.barcode)
        }
    }

    private fun onBarcodeScanned(barcode: String) {
        scans.compute(barcode) { _, amount -> amount?.inc() ?: 1 }

        if (scans.getValue(barcode) > REQUIRED_SCANS &&
            found.compareAndSet(expectedValue = false, newValue = true)
        ) {
            viewModelScope.launch {
                _effect.send(ScanEffect.Found(barcode))
            }
        }
    }
}