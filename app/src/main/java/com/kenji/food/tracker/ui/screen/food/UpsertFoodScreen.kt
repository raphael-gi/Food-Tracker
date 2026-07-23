package com.kenji.food.tracker.ui.screen.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.InputChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.result.ResultEffect
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.ui.component.BarcodeScannedInfo
import com.kenji.food.tracker.ui.component.FullScreenLoading
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.component.button.ScanButton
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.component.input.FormTextField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.food.add.UpsertFoodAction
import com.kenji.food.tracker.ui.viewmodel.food.add.UpsertFoodEffect
import com.kenji.food.tracker.ui.viewmodel.food.add.UpsertFoodViewModel

@Composable
fun UpsertFoodScreen(
    viewModel: UpsertFoodViewModel,
    onLaunchScanner: () -> Unit,
    onNavBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResultEffect<String> { code ->
        viewModel.onAction(UpsertFoodAction.SetCode(code))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                UpsertFoodEffect.NavBack -> onNavBack()
            }
        }
    }

    val title = if (state.isCreate) R.string.create else R.string.edit

    when {
        state.isLoading -> FullScreenLoading()
        else -> Scaffold(
            topBar = { TopBar(title, onBackPressed = onNavBack) },
            floatingActionButton = {
                ScanButton(onClick = onLaunchScanner)
            }
        ) { innerPadding ->
            UpsertFood(
                modifier = Modifier.padding(innerPadding),
                name = state.name,
                calories = state.calories,
                carbs = state.carbs,
                protein = state.proteins,
                fats = state.fats,
                sugar = state.sugar,
                foodUnit = state.unit,
                quantity = state.quantity,
                code = state.code,
                isCreate = state.isCreate,
                onAction = viewModel::onAction
            )
        }
    }
}

@Composable
private fun UpsertFood(
    modifier: Modifier,
    name: String,
    calories: Int?,
    carbs: Double?,
    protein: Double?,
    fats: Double?,
    sugar: Double?,
    foodUnit: FoodUnit?,
    quantity: Double?,
    code: String?,
    isCreate: Boolean,
    onAction: (UpsertFoodAction) -> Unit
) {
    val scroll = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (isCreate) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FormTextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = name,
            label = R.string.name,
            required = true,
            onValueChange = { onAction(UpsertFoodAction.SetName(it)) }
        )

        FormNumberField(
            value = calories,
            label = R.string.caloriesLabel,
            iconRes = R.drawable.calories,
            required = true,
            onValueChange = { onAction(UpsertFoodAction.SetCalories(it)) }
        )

        FormNumberField(
            value = protein,
            label = R.string.protein,
            onValueChange = { onAction(UpsertFoodAction.SetProteins(it)) }
        )

        FormNumberField(
            value = carbs,
            label = R.string.carbs,
            iconRes = R.drawable.carbs,
            onValueChange = { onAction(UpsertFoodAction.SetCarbs(it)) }
        )

        FormNumberField(
            value = fats,
            label = R.string.fat,
            onValueChange = { onAction(UpsertFoodAction.SetFats(it)) }
        )

        FormNumberField(
            value = sugar,
            label = R.string.sugar,
            onValueChange = { onAction(UpsertFoodAction.SetSugar(it)) }
        )

        Row {
            FoodUnit.entries.forEach { unit ->
                InputChip(
                    selected = unit == foodUnit,
                    label = { Text(text = unit.name.lowercase()) },
                    onClick = { onAction(UpsertFoodAction.SetFoodUnit(unit)) }
                )
            }
        }

        val quantityLabel =
            if (foodUnit != null) stringResource(R.string.quantityUnitLabel, foodUnit)
            else stringResource(R.string.quantityLabel)

        FormNumberField(value = quantity, label = quantityLabel) {
            onAction(UpsertFoodAction.SetQuantity(it))
        }

        if (code != null) {
            BarcodeScannedInfo {
                onAction(UpsertFoodAction.RemoveCode)
            }
        }

        val title = if (isCreate) R.string.create else R.string.edit

        ActionButton(modifier = Modifier.padding(vertical = 10.dp), text = title) {
            onAction(UpsertFoodAction.Create)
        }
    }
}


@Preview(widthDp = 500)
@Composable
private fun UpsertFoodPreview() {
    FoodTrackerTheme {
        Surface {
            UpsertFood(
                Modifier,
                "",
                null,
                null,
                null,
                5.0,
                10.0,
                FoodUnit.G,
                5.0,
                code = "",
                isCreate = true
            ) { }
        }
    }
}
