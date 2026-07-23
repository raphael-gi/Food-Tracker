package com.kenji.food.tracker.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.component.button.ScanButton
import com.kenji.food.tracker.ui.component.button.SelectionButton

@Composable
fun FoodSelectorButtons(
    modifier: Modifier = Modifier,
    onClickScanButton: () -> Unit,
    onClickSelectButton: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        SelectionButton(
            modifier = Modifier.weight(1f),
            text = R.string.selectFood,
            onClick = onClickSelectButton
        )

        ScanButton(
            modifier = Modifier.padding(10.dp),
            onClick = onClickScanButton
        )
    }
}