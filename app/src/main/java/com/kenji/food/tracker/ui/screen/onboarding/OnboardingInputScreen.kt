package com.kenji.food.tracker.ui.screen.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.onboarding.input.OnboardingInputAction
import com.kenji.food.tracker.ui.viewmodel.onboarding.input.OnboardingInputEffect
import com.kenji.food.tracker.ui.viewmodel.onboarding.input.OnboardingInputViewModel

@Composable
fun OnboardingInputScreen(
    viewModel: OnboardingInputViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                OnboardingInputEffect.FinishOnboardingInput -> onFinish()
            }
        }
    }

    OnboardingInput(
        calorieTarget = state.calorieTarget,
        proteinTarget = state.proteinTarget,
        sugarTarget = state.sugarTarget,
        onAction = viewModel::onAction
    )
}

@Composable
private fun OnboardingInput(
    modifier: Modifier = Modifier,
    calorieTarget: Int?,
    proteinTarget: Int?,
    sugarTarget: Int?,
    onAction: (OnboardingInputAction) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.caloricTargetQuestion),
            style = MaterialTheme.typography.titleMedium
        )
        FormNumberField(
            value = calorieTarget,
            label = R.string.calorieTargetLabel,
            iconRes = R.drawable.calories
        ) {
            onAction(OnboardingInputAction.SetCalorieTarget(it))
        }

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = stringResource(R.string.add)
                )
                Text("Specify More")
                HorizontalDivider()
            }

            FormNumberField(value = proteinTarget, label = R.string.proteinTargetLabel) {
                onAction(OnboardingInputAction.SetProteinTarget(it))
            }

            FormNumberField(value = sugarTarget, label = R.string.sugarTargetLabel) {
                onAction(OnboardingInputAction.SetSugarTarget(it))
            }
        }

        ActionButton(
            text = R.string.finish
        ) {
            onAction(OnboardingInputAction.Finish)
        }
    }
}


@Preview
@Composable
private fun OnboardingInputPreview() {
    FoodTrackerTheme {
        Surface {
            OnboardingInput(calorieTarget = 2500, proteinTarget = null, sugarTarget = 5) {}
        }
    }
}