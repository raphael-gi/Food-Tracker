package com.kenji.food.tracker.ui.component.button

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kenji.food.tracker.R
import com.kenji.food.tracker.util.Permissions

@Composable
fun ScanButton(onClick: () -> Unit) {
    val context = LocalContext.current

    val hasPermissions = remember { Permissions.hasBarcodePermissions(context) }

    if (hasPermissions) {
        FloatingActionButton(onClick = onClick) {
            Icon(
                painter = painterResource(R.drawable.scan),
                contentDescription = stringResource(R.string.scan)
            )
        }
    }
}
