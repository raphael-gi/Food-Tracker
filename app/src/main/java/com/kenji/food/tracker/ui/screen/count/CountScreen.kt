package com.kenji.food.tracker.ui.screen.count

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.result.ResultEffect
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.ui.component.FoodCard
import com.kenji.food.tracker.ui.component.FoodSelectorButtons
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.component.cell.FoodCell
import com.kenji.food.tracker.ui.component.cell.RecipeCell
import com.kenji.food.tracker.ui.component.info.NoData
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.component.input.SearchField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.count.CountAction
import com.kenji.food.tracker.ui.viewmodel.count.CountEffect
import com.kenji.food.tracker.ui.viewmodel.count.CountViewModel
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun CountScreen(
    viewModel: CountViewModel = hiltViewModel(),
    onLaunchCamera: () -> Unit,
    onFinish: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val foods = viewModel.meals.collectAsLazyPagingItems()

    ResultEffect<String> { code ->
        viewModel.onAction(CountAction.CodeScanned(code))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CountEffect.LaunchCamera -> onLaunchCamera()
                CountEffect.Finish -> onFinish()
                CountEffect.ScanNotFound -> {}
            }
        }
    }

    Scaffold(
        topBar = { TopBar(title = R.string.countMeal) },
    ) { innerPadding ->
        CountContent(
            modifier = Modifier.padding(innerPadding),
            foods = foods,
            selectedMeal = state.selectedMeal,
            countedMeal = state.countedMeal,
            quantity = state.quantity,
            eatenAt = state.eatenAt,
            isSelectMealMode = state.isSelectMealMode,
            isSelectDateMode = state.isSelectDateMode,
            query = state.query,
            onAction = viewModel::onAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountContent(
    modifier: Modifier = Modifier,
    foods: LazyPagingItems<Recipe>,
    selectedMeal: Recipe?,
    countedMeal: CountedMealEntity?,
    quantity: Double?,
    eatenAt: Long,
    isSelectMealMode: Boolean,
    isSelectDateMode: Boolean,
    query: String,
    onAction: (CountAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FoodSelectorButtons(
            onClickSelectButton = { onAction(CountAction.ToggleSelectMealMode) },
            onClickScanButton = { onAction(CountAction.LaunchCamera) }
        )

        AnimatedVisibility(selectedMeal != null) {
            if (selectedMeal != null && countedMeal != null) {
                CountedMealOverview(
                    recipe = selectedMeal,
                    countedMeal = countedMeal,
                    quantity = quantity,
                    eatenAt = eatenAt,
                    onAction = onAction
                )
            }
        }

        when {
            isSelectMealMode -> {
                ModalBottomSheet(
                    onDismissRequest = { onAction(CountAction.ToggleSelectMealMode) },
                    sheetState = sheetState
                ) {
                    SelectionList(foods, query, onAction)
                }
            }

            isSelectDateMode -> {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDate = LocalDateTime
                        .ofInstant(Instant.ofEpochMilli(eatenAt), ZoneOffset.systemDefault())
                        .toLocalDate(),
                    selectableDates = object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                            return utcTimeMillis <= Instant.now().toEpochMilli()
                        }
                    }
                )

                DatePickerDialog(
                    onDismissRequest = { onAction(CountAction.ToggleSelectDateMode) },
                    confirmButton = {
                        TextButton(
                            onClick = { onAction(CountAction.SelectDate(datePickerState.selectedDateMillis)) }
                        ) {
                            Text(stringResource(R.string.ok))
                        }
                    },
                ) {
                    DatePicker(datePickerState)
                }
            }
        }
    }
}

@Composable
private fun CountedMealOverview(
    recipe: Recipe,
    countedMeal: CountedMealEntity,
    quantity: Double?,
    eatenAt: Long,
    onAction: (CountAction) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            FormNumberField(
                modifier = Modifier.weight(1f),
                value = quantity,
                label = stringResource(
                    R.string.quantityUnitLabel,
                    recipe.food.unit.toString().lowercase()
                ),
                onValueChange = {
                    onAction(CountAction.SetMealQuantity(it))
                }
            )

            Button(
                modifier = Modifier
                    .heightIn(min = TextFieldDefaults.MinHeight)
                    .widthIn(min = TextFieldDefaults.MinHeight),
                shape = ShapeDefaults.Small,
                onClick = { onAction(CountAction.ToggleSelectDateMode) }
            ) {
                val isDateToday = remember(eatenAt) {
                    LocalDateTime
                        .ofInstant(Instant.ofEpochMilli(eatenAt), ZoneId.systemDefault())
                        .toLocalDate()
                        .isEqual(LocalDate.now())
                }

                val dateIcon = if (isDateToday) R.drawable.calendar_today
                else R.drawable.calendar_check

                Icon(
                    painter = painterResource(dateIcon),
                    contentDescription = stringResource(R.string.date)
                )
            }
        }

        FoodCard(
            modifier = Modifier.padding(10.dp),
            name = countedMeal.name,
            calories = countedMeal.calories,
            protein = countedMeal.protein,
            carbs = countedMeal.carbs,
            sugar = countedMeal.sugar,
            fats = countedMeal.fats,
            saturatedFats = countedMeal.saturatedFats
        )

        ActionButton(text = R.string.confirm) {
            onAction(CountAction.CountMeal)
        }
    }
}

@Composable
private fun SelectionList(
    items: LazyPagingItems<Recipe>,
    query: String,
    onAction: (CountAction) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchField(query = query, placeholder = R.string.searchMeals) {
            onAction(CountAction.Search(it))
        }
        if (items.loadState.isIdle && items.itemCount == 0) {
            NoData(
                icon = R.drawable.food,
                iconDescription = R.string.food,
                text = R.string.noFoods
            )
        } else {
            LazyColumn {
                items(count = items.itemCount, key = items.itemKey { it.food.id }) { index ->
                    val item = items[index]
                    if (item != null) {
                        FoodSelectionCell(item) {
                            onAction(CountAction.SelectMeal(item))
                        }
                    } else {
                        Text("Unavailable")
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodSelectionCell(item: Recipe, onSelect: () -> Unit) {
    if (item.food.isRecipe) {
        RecipeCell(
            modifier = Modifier.clickable { onSelect() },
            item = item
        )
    } else {
        FoodCell(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelect() },
            item = item.food
        )
    }
}


@Preview
@Composable
private fun CountContentPreview() {
    val foods = flowOf(
        PagingData.from(
            listOf<Recipe>()
        )
    ).collectAsLazyPagingItems()

    val selectedMeal = Recipe(
        food = FoodEntity(
            id = 0, isRecipe = false,
            name = "Chicken",
            calories = 123,
            protein = 20.0,
            quantity = 100.0,
            unit = FoodUnit.G
        ),
        foods = emptyList()
    )

    FoodTrackerTheme {
        Surface {
            CountContent(
                foods = foods,
                selectedMeal = selectedMeal,
                countedMeal = CountedMealEntity(
                    id = 0,
                    name = "abc",
                    calories = 5,
                    fats = 5.0,
                    saturatedFats = 5.0
                ),
                quantity = 5.0,
                eatenAt = Instant.now().toEpochMilli(),
                isSelectMealMode = false,
                isSelectDateMode = false,
                query = ""
            ) { }
        }
    }
}
