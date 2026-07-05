package com.kenji.food.tracker.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kenji.food.tracker.ui.component.button.NavigationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    @StringRes title: Int,
    actions: @Composable RowScope.() -> Unit = {},
    onBackPressed: (() -> Unit)? = null
) {
    TopAppBar(
        navigationIcon = {
            onBackPressed?.let { NavigationButton(onBackPressed) }
        },
        title = {
            Text(stringResource(title))
        },
        actions = actions
    )
}