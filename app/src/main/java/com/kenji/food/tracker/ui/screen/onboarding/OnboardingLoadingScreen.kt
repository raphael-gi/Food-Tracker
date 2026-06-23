package com.kenji.food.tracker.ui.screen.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.onboarding.start.OnboardingLoadingEffect
import com.kenji.food.tracker.ui.viewmodel.onboarding.start.OnboardingLoadingViewModel

@Composable
fun OnboardingLoadingScreen(
    viewModel: OnboardingLoadingViewModel = hiltViewModel(),
    continueOnboarding: () -> Unit,
    skipOnboarding: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                OnboardingLoadingEffect.ContinueOnboarding -> continueOnboarding()
                OnboardingLoadingEffect.SkipOnboarding -> skipOnboarding()
            }
        }
    }

    OnboardingLoadingContent()
}

@Composable
fun OnboardingLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Preview
@Composable
private fun OnboardingLoadingPreview() {
    FoodTrackerTheme {
        Surface {
            OnboardingLoadingContent()
        }
    }
}
