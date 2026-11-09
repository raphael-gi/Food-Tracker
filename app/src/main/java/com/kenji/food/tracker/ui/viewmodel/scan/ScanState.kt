package com.kenji.food.tracker.ui.viewmodel.scan

sealed interface ScanAction {
    data class BarcodeScanned(val barcode: String) : ScanAction
}

sealed interface ScanEffect {
    data class Found(val code: String) : ScanEffect
}
