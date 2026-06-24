package com.kenji.food.tracker.ui.component.button

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kenji.food.tracker.R

@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            painter = painterResource(R.drawable.add),
            contentDescription = stringResource(R.string.add)
        )
    }
}