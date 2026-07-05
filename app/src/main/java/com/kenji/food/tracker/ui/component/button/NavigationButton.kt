package com.kenji.food.tracker.ui.component.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import com.kenji.food.tracker.R

@Composable
fun NavigationButton(onBackPressed: () -> Unit) {
    IconButton(onClick = dropUnlessResumed { onBackPressed() }) {
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = stringResource(R.string.navigateBack)
        )
    }
}