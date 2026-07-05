package com.kenji.food.tracker.ui.screen.onboarding

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.component.FullScreenLoading
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.component.button.SelectionButton
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.onboarding.OnboardingAction
import com.kenji.food.tracker.ui.viewmodel.onboarding.OnboardingEffect
import com.kenji.food.tracker.ui.viewmodel.onboarding.OnboardingStep
import com.kenji.food.tracker.ui.viewmodel.onboarding.OnboardingViewModel
import com.kenji.food.tracker.util.Permissions

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

    BackHandler(enabled = state.currentStep != OnboardingStep.LOADING && state.currentStep != OnboardingStep.CALORIE_INPUT) {
        viewModel.onAction(OnboardingAction.StepBack)
    }

    OnboardingContent(
        currentScreen = state.currentStep,
        calorieTarget = state.calorieTarget,
        onAction = viewModel::onAction
    )
}

@Composable
private fun OnboardingContent(
    currentScreen: OnboardingStep,
    calorieTarget: Int?,
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
            OnboardingStep.LOADING -> FullScreenLoading()
            OnboardingStep.CALORIE_INPUT -> CalorieInput(
                calorieTarget = calorieTarget,
                onAction = onAction
            )

            OnboardingStep.SCAN_PERMISSION -> ScanPermission(onAction)
        }
    }
}

@Composable
private fun CalorieInput(calorieTarget: Int?, onAction: (OnboardingAction) -> Unit) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ActionButton(text = R.string.onboardingContinue) {
                onAction(OnboardingAction.ConfirmCalories)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize(),
        ) {
            Column {
                Text(
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = stringResource(R.string.slogan),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.caloricTargetQuestion),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    FormNumberField(
                        value = calorieTarget,
                        label = R.string.calorieTargetLabel,
                        iconRes = R.drawable.calories,
                        onPressDone = { onAction(OnboardingAction.ConfirmCalories) },
                        onValueChange = { onAction(OnboardingAction.SetCalorieTarget(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ScanPermission(onAction: (OnboardingAction) -> Unit) {
    val permissionLauncher = rememberLauncherForActivityResult(RequestPermission()) {
        onAction(OnboardingAction.Finish)
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SelectionButton(text = R.string.skip) {
                    onAction(OnboardingAction.Finish)
                }

                ActionButton(text = R.string.allow) {
                    permissionLauncher.launch(Permissions.BARCODE_PERMISSIONS)
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    text = stringResource(R.string.stopWastingTime),
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = stringResource(R.string.onboardingScanning),
                    style = MaterialTheme.typography.headlineSmall
                )
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
                currentScreen = OnboardingStep.CALORIE_INPUT,
                calorieTarget = 5,
                onAction = {}
            )
        }
    }
}
