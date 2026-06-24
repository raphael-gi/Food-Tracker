package com.kenji.food.tracker.ui.screen.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme

@Composable
fun OnboardingStartScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Track your calories...",
            style = MaterialTheme.typography.headlineMedium
        )

        ActionButton(
            text = R.string.start,
            onClick = onStart
        )
    }
}


@Preview
@Composable
private fun OnboardingStartPreview() {
    FoodTrackerTheme {
        Surface {
            OnboardingStartScreen {}
        }
    }
}
