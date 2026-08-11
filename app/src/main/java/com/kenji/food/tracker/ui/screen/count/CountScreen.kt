package com.kenji.food.tracker.ui.screen.count

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.kenji.food.tracker.util.Formatter
import kotlinx.coroutines.flow.flowOf

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
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ActionButton(text = R.string.confirm) {
                viewModel.onAction(CountAction.CountMeal)
            }
        }
    ) { innerPadding ->
        CountContent(
            modifier = Modifier.padding(innerPadding),
            foods = foods,
            selectedMeal = state.selectedMeal,
            countedMeal = state.countedMeal,
            quantity = state.quantity,
            isSelectMode = state.isSelectMode,
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
    isSelectMode: Boolean,
    query: String,
    onAction: (CountAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AnimatedVisibility(selectedMeal != null) {
            selectedMeal?.let { selectedMeal ->
                countedMeal?.let {
                    FoodCard(
                        recipe = selectedMeal,
                        countedMeal = countedMeal,
                        quantity = quantity,
                        onSetQuantity = { onAction(CountAction.SetMealQuantity(it)) }
                    )
                }
            }
        }

        FoodSelectorButtons(
            onClickSelectButton = { onAction(CountAction.ToggleSelectMode) },
            onClickScanButton = { onAction(CountAction.LaunchCamera) }
        )

        if (isSelectMode) {
            ModalBottomSheet(
                onDismissRequest = { onAction(CountAction.ToggleSelectMode) },
                sheetState = sheetState
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    SelectionList(foods, query, onAction)
                }
            }
        }
    }
}

@Composable
private fun FoodCard(
    recipe: Recipe,
    countedMeal: CountedMealEntity,
    quantity: Double?,
    onSetQuantity: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = recipe.food.name,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "${countedMeal.calories.toString()} kcal",
            style = MaterialTheme.typography.labelLarge
        )

        countedMeal.fats?.let { fats ->
            Text(
                text = "Fats: ${Formatter.formatDecimal(fats)}g",
                style = MaterialTheme.typography.labelLarge
            )
        }

        countedMeal.saturatedFats?.let { saturatedFats ->
            Text(
                text = "Saturated Fats: ${Formatter.formatDecimal(saturatedFats)}g",
                style = MaterialTheme.typography.labelLarge
            )
        }

        countedMeal.carbs?.let { carbs ->
            Text(
                text = "Carbs: ${Formatter.formatDecimal(carbs)}g",
                style = MaterialTheme.typography.labelLarge
            )
        }

        countedMeal.sugar?.let { sugar ->
            Text(
                text = stringResource(R.string.sugarLabel, Formatter.formatDecimal(sugar)),
                style = MaterialTheme.typography.labelLarge
            )
        }

        countedMeal.protein?.let { protein ->
            Text(
                text = "Protein: ${Formatter.formatDecimal(protein)}g",
                style = MaterialTheme.typography.labelLarge
            )
        }

        FormNumberField(
            value = quantity,
            label = stringResource(
                R.string.quantityUnitLabel,
                recipe.food.unit.toString().lowercase()
            ),
            onValueChange = onSetQuantity
        )
    }
}

@Composable
private fun SelectionList(
    items: LazyPagingItems<Recipe>,
    query: String,
    onAction: (CountAction) -> Unit
) {
    Column {
        SearchField(query = query) {
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
                isSelectMode = false,
                query = ""
            ) { }
        }
    }
}
