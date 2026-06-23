package com.kenji.food.tracker.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.component.ActionButton
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.viewmodel.food.target.FoodTargetAction
import com.kenji.food.tracker.ui.viewmodel.food.target.FoodTargetEffect
import com.kenji.food.tracker.ui.viewmodel.food.target.FoodTargetViewModel

@Composable
fun FoodTargetScreen(
    viewModel: FoodTargetViewModel,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                FoodTargetEffect.NavBack -> onBackPressed()
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(R.string.foodTarget, onBackPressed = onBackPressed)
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ActionButton(text = R.string.update) {
                viewModel.onAction(FoodTargetAction.Update)
            }
        }
    ) { innerPadding ->
        FoodTargetContent(
            modifier = Modifier.padding(innerPadding),
            calories = state.calories,
            proteins = state.proteins,
            sugar = state.sugar,
            viewModel::onAction
        )
    }
}

@Composable
fun FoodTargetContent(
    modifier: Modifier = Modifier,
    calories: Int?,
    proteins: Int?,
    sugar: Int?,
    onAction: (FoodTargetAction) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FormNumberField(
            value = calories,
            label = R.string.caloriesLabel,
            iconRes = R.drawable.calories
        ) {
            onAction(FoodTargetAction.SetCalories(it))
        }
        FormNumberField(value = proteins, label = R.string.proteinLabel) {
            onAction(FoodTargetAction.SetProteins(it))
        }
        FormNumberField(value = sugar, label = R.string.sugarLabel) {
            onAction(FoodTargetAction.SetSugar(it))
        }
    }
}
