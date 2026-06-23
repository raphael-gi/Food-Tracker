package com.kenji.food.tracker.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme

@Composable
fun ActionButton(modifier: Modifier = Modifier, @StringRes text: Int, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        shape = FloatingActionButtonDefaults.smallShape,
        onClick = onClick
    ) {
        Text(stringResource(text))
    }
}


@Preview(showBackground = true, heightDp = 700)
@Composable
private fun PreviewActionButton() {
    FoodTrackerTheme {
        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                ActionButton(text = R.string.add) { }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding))
        }
    }
}