package com.kenji.food.tracker.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object Permissions {
    fun hasBarcodePermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            BARCODE_PERMISSIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    const val BARCODE_PERMISSIONS = Manifest.permission.CAMERA
}