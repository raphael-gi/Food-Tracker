package com.kenji.food.tracker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Transparent,
) {
    Box(
        modifier = modifier
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = AssistChipDefaults.shape
            )
            .background(
                color = color,
                shape = AssistChipDefaults.shape
            )
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, 5.dp),
            text = text,
            color = AssistChipDefaults.assistChipColors().labelColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview
@Composable
private fun TagPreview() {
    FoodTrackerTheme {
        Surface {
            Tag(text = "12355555")
        }
    }
}