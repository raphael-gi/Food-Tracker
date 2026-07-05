package com.kenji.food.tracker.ui.component.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kenji.food.tracker.R

@Composable
fun EditButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.edit),
            contentDescription = stringResource(R.string.edit)
        )
    }
}