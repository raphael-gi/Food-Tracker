package com.kenji.food.tracker.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import com.kenji.food.tracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    @StringRes title: Int,
    actions: @Composable RowScope.() -> Unit = {},
    onBackPressed: (() -> Unit)? = null
) {
    TopAppBar(
        navigationIcon = {
            onBackPressed?.let {
                IconButton(
                    onClick = dropUnlessResumed {
                        it()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = stringResource(R.string.navigateBack)
                    )
                }
            }
        },
        title = {
            Text(stringResource(title))
        },
        actions = actions
    )
}