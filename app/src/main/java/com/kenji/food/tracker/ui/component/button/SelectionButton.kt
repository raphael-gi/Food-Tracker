package com.kenji.food.tracker.ui.component.button

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme

@Composable
fun SelectionButton(modifier: Modifier = Modifier, @StringRes text: Int, onClick: () -> Unit) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = ShapeDefaults.Small,
        onClick = onClick
    ) {
        Text(
            text = stringResource(text),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}


@Preview(heightDp = 200, widthDp = 500)
@Composable
private fun SelectionButtonPreview() {
    FoodTrackerTheme {
        Surface {
            Box {
                SelectionButton(text = R.string.selectFood) { }
            }
        }
    }
}