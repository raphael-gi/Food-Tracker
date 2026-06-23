package com.kenji.food.tracker.ui.screen.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.ui.component.ActionButton
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.component.input.FormTextField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.food.add.AddFoodAction
import com.kenji.food.tracker.ui.viewmodel.food.add.AddFoodEffect
import com.kenji.food.tracker.ui.viewmodel.food.add.AddFoodViewModel

@Composable
fun AddFoodScreen(viewModel: AddFoodViewModel = hiltViewModel(), onNavBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddFoodEffect.NavBack -> onNavBack()
            }
        }
    }

    Scaffold(
        topBar = { TopBar(R.string.add, onBackPressed = { onNavBack() }) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ActionButton(text = R.string.add) {
                viewModel.onAction(AddFoodAction.Create)
            }
        }
    ) { innerPadding ->
        AddFood(
            modifier = Modifier.padding(innerPadding),
            name = state.name,
            calories = state.calories,
            carbs = state.carbs,
            protein = state.proteins,
            fats = state.fats,
            foodUnit = state.unit,
            quantity = state.quantity,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun AddFood(
    modifier: Modifier,
    name: String,
    calories: Int?,
    carbs: Int?,
    protein: Int?,
    fats: Int?,
    foodUnit: FoodUnit?,
    quantity: Int?,
    onAction: (AddFoodAction) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier.padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FormTextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = name,
            label = R.string.name,
            required = true,
            onValueChange = { onAction(AddFoodAction.SetName(it)) }
        )

        FormNumberField(
            value = calories,
            label = R.string.caloriesLabel,
            iconRes = R.drawable.calories,
            required = true,
            onValueChange = { onAction(AddFoodAction.SetCalories(it)) }
        )

        FormNumberField(
            value = protein,
            label = R.string.protein,
            onValueChange = { onAction(AddFoodAction.SetProteins(it)) }
        )

        FormNumberField(
            value = carbs,
            label = R.string.carbs,
            iconRes = R.drawable.carbs,
            onValueChange = { onAction(AddFoodAction.SetCarbs(it)) }
        )

        FormNumberField(
            value = fats,
            label = R.string.fat,
            onValueChange = { onAction(AddFoodAction.SetFats(it)) }
        )

        Row {
            FoodUnit.entries.forEach { unit ->
                InputChip(
                    selected = unit == foodUnit,
                    label = { Text(text = unit.name.lowercase()) },
                    onClick = { onAction(AddFoodAction.SetFoodUnit(unit)) }
                )
            }
        }

        val quantityLabel =
            if (foodUnit != null) stringResource(R.string.quantityUnitLabel, foodUnit)
            else stringResource(R.string.quantityLabel)

        FormNumberField(value = quantity, label = quantityLabel) {
            onAction(AddFoodAction.SetQuantity(it))
        }
    }
}

@Preview(widthDp = 500)
@Composable
private fun AddFoodPreview() {
    FoodTrackerTheme {
        Surface {
            AddFood(Modifier, "", null, null, null, 5, FoodUnit.G, 5) { }
        }
    }
}
