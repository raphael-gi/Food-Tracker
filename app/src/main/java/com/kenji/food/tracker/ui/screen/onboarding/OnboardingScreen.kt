package com.kenji.food.tracker.ui.screen.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.onboarding.OnboardingAction
import com.kenji.food.tracker.ui.viewmodel.onboarding.OnboardingEffect
import com.kenji.food.tracker.ui.viewmodel.onboarding.OnboardingStep
import com.kenji.food.tracker.ui.viewmodel.onboarding.OnboardingViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                OnboardingEffect.FinishOnboarding -> onFinish()
            }
        }
    }

    BackHandler(enabled = state.currentStep != OnboardingStep.LOADING && state.currentStep != OnboardingStep.START) {
        viewModel.onAction(OnboardingAction.StepBack)
    }

    OnboardingContent(
        currentScreen = state.currentStep,
        calorieTarget = state.calorieTarget,
        proteinTarget = state.proteinTarget,
        sugarTarget = state.sugarTarget,
        onAction = viewModel::onAction
    )
}

@Composable
private fun OnboardingContent(
    currentScreen: OnboardingStep,
    calorieTarget: Int?,
    proteinTarget: Int?,
    sugarTarget: Int?,
    onAction: (OnboardingAction) -> Unit
) {
    val pagerState = rememberPagerState { OnboardingStep.entries.size }

    LaunchedEffect(currentScreen) {
        pagerState.animateScrollToPage(currentScreen.ordinal)
    }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false,
        state = pagerState
    ) { pageIndex ->
        when (OnboardingStep.entries[pageIndex]) {
            OnboardingStep.LOADING -> Loading()
            OnboardingStep.START -> Start { onAction(OnboardingAction.Start) }
            OnboardingStep.CALORIE_INPUT -> CalorieInput(
                calorieTarget = calorieTarget,
                onAction = onAction
            )

            OnboardingStep.ADDITIONAL_INPUT -> AdditionalInput(
                proteinTarget = proteinTarget,
                sugarTarget = sugarTarget,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Start(onClickStart: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = stringResource(R.string.slogan),
                style = MaterialTheme.typography.headlineMedium
            )

            ActionButton(text = R.string.start, onClick = onClickStart)
        }
    }
}

@Composable
private fun CalorieInput(calorieTarget: Int?, onAction: (OnboardingAction) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.caloricTargetQuestion),
                style = MaterialTheme.typography.headlineMedium
            )

            FormNumberField(
                value = calorieTarget,
                label = R.string.calorieTargetLabel,
                iconRes = R.drawable.calories,
                onValueChange = { onAction(OnboardingAction.SetCalorieTarget(it)) }
            )

            ActionButton(text = R.string.onboardingContinue) {
                onAction(OnboardingAction.ConfirmCalories)
            }
        }
    }
}

@Composable
private fun AdditionalInput(
    proteinTarget: Int?,
    sugarTarget: Int?,
    onAction: (OnboardingAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.additionalTargetQuestion),
                style = MaterialTheme.typography.headlineMedium
            )

            FormNumberField(value = proteinTarget, label = R.string.proteinTargetLabel) {
                onAction(OnboardingAction.SetProteinTarget(it))
            }

            FormNumberField(value = sugarTarget, label = R.string.sugarTargetLabel) {
                onAction(OnboardingAction.SetSugarTarget(it))
            }

            ActionButton(text = R.string.finish) {
                onAction(OnboardingAction.Finish)
            }
        }
    }
}


@Preview
@Composable
private fun OnboardingScreenPreview() {
    FoodTrackerTheme {
        Surface {
            OnboardingContent(
                currentScreen = OnboardingStep.ADDITIONAL_INPUT,
                calorieTarget = 5,
                proteinTarget = null,
                sugarTarget = 10,
                onAction = {}
            )
        }
    }
}
