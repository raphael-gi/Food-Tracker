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
import com.kenji.food.tracker.entity.FoodCategory
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.ui.component.BarcodeScannedInfo
import com.kenji.food.tracker.ui.component.FullScreenLoading
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.component.button.ScanButton
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.component.input.FormTextField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.food.modify.ModifyFoodAction
import com.kenji.food.tracker.ui.viewmodel.food.modify.ModifyFoodEffect
import com.kenji.food.tracker.ui.viewmodel.food.modify.ModifyFoodViewModel
import com.kenji.food.tracker.util.ModifyMode

@Composable
fun ModifyFoodScreen(
    viewModel: ModifyFoodViewModel,
    onLaunchScanner: () -> Unit,
    onNavBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResultEffect<String> { code ->
        viewModel.onAction(ModifyFoodAction.SetCode(code))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ModifyFoodEffect.NavBack -> onNavBack()
            }
        }
    }

    val title = when (state.mode) {
        is ModifyMode.Edit -> R.string.edit
        is ModifyMode -> R.string.create
    }

    when {
        state.isLoading -> FullScreenLoading()
        else -> Scaffold(
            topBar = { TopBar(title, onBackPressed = onNavBack) },
            floatingActionButton = {
                ScanButton(onClick = onLaunchScanner)
            }
        ) { innerPadding ->
            ModifyFood(
                modifier = Modifier.padding(innerPadding),
                name = state.name,
                calories = state.calories,
                fats = state.fats,
                saturatedFats = state.saturatedFats,
                carbs = state.carbs,
                sugar = state.sugar,
                protein = state.proteins,
                foodUnit = state.unit,
                foodCategory = state.category,
                quantity = state.quantity,
                code = state.code,
                mode = state.mode,
                onAction = viewModel::onAction
            )
        }
    }
}

@Composable
private fun ModifyFood(
    modifier: Modifier,
    name: String,
    calories: Int?,
    fats: Double?,
    saturatedFats: Double?,
    carbs: Double?,
    sugar: Double?,
    protein: Double?,
    foodUnit: FoodUnit?,
    foodCategory: FoodCategory?,
    quantity: Double?,
    code: String?,
    mode: ModifyMode,
    onAction: (ModifyFoodAction) -> Unit
) {
    val scroll = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (mode is ModifyMode.Create) {
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
            onValueChange = { onAction(ModifyFoodAction.SetName(it)) }
        )

        FormNumberField(
            value = calories,
            label = R.string.caloriesLabel,
            iconRes = R.drawable.calories,
            required = true,
            onValueChange = { onAction(ModifyFoodAction.SetCalories(it)) }
        )

        FormNumberField(
            value = fats,
            label = R.string.fat,
            iconRes = R.drawable.fat,
            onValueChange = { onAction(ModifyFoodAction.SetFats(it)) }
        )

        FormNumberField(
            value = saturatedFats,
            label = R.string.saturatedFat,
            iconRes = R.drawable.saturated_fat,
            onValueChange = { onAction(ModifyFoodAction.SetSaturatedFats(it)) }
        )

        FormNumberField(
            value = carbs,
            label = R.string.carbs,
            iconRes = R.drawable.carbs,
            onValueChange = { onAction(ModifyFoodAction.SetCarbs(it)) }
        )

        FormNumberField(
            value = sugar,
            label = R.string.sugar,
            iconRes = R.drawable.sugar,
            onValueChange = { onAction(ModifyFoodAction.SetSugar(it)) }
        )

        FormNumberField(
            value = protein,
            label = R.string.protein,
            iconRes = R.drawable.protein,
            onValueChange = { onAction(ModifyFoodAction.SetProteins(it)) }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            FoodCategory.entries.forEach { category ->
                InputChip(
                    selected = category == foodCategory,
                    label = { Text(text = category.name.lowercase()) },
                    onClick = { onAction(ModifyFoodAction.SetFoodCategory(category)) }
                )
            }
        }

        Row {
            FoodUnit.entries.forEach { unit ->
                InputChip(
                    selected = unit == foodUnit,
                    label = { Text(text = unit.name.lowercase()) },
                    onClick = { onAction(ModifyFoodAction.SetFoodUnit(unit)) }
                )
            }
        }

        val quantityLabel =
            if (foodUnit != null) stringResource(R.string.quantityUnitLabel, foodUnit)
            else stringResource(R.string.quantityLabel)

        FormNumberField(value = quantity, label = quantityLabel) {
            onAction(ModifyFoodAction.SetQuantity(it))
        }

        if (code != null) {
            BarcodeScannedInfo {
                onAction(ModifyFoodAction.RemoveCode)
            }
        }

        val title = when (mode) {
            is ModifyMode.Edit -> R.string.edit
            is ModifyMode -> R.string.create
        }


        ActionButton(modifier = Modifier.padding(vertical = 10.dp), text = title) {
            onAction(ModifyFoodAction.Create)
        }
    }
}


@Preview(widthDp = 500)
@Composable
private fun ModifyFoodPreview() {
    FoodTrackerTheme {
        Surface {
            ModifyFood(
                Modifier,
                "",
                null,
                null,
                null,
                null,
                5.0,
                10.0,
                FoodUnit.G,
                foodCategory = null,
                5.0,
                code = "",
                mode = ModifyMode.Create
            ) { }
        }
    }
}
