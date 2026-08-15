package com.kenji.food.tracker.ui.screen.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.result.ResultEffect
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.entity.RecipeFoodEntity
import com.kenji.food.tracker.ui.component.FoodSelectorButtons
import com.kenji.food.tracker.ui.component.FullScreenLoading
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.component.cell.FoodCell
import com.kenji.food.tracker.ui.component.info.NoData
import com.kenji.food.tracker.ui.component.input.FormNumberField
import com.kenji.food.tracker.ui.component.input.FormTextField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.recipe.add.UpsertRecipeAction
import com.kenji.food.tracker.ui.viewmodel.recipe.add.UpsertRecipeEffect
import com.kenji.food.tracker.ui.viewmodel.recipe.add.UpsertRecipeViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun UpsertRecipeScreen(
    viewModel: UpsertRecipeViewModel,
    onLaunchCamera: () -> Unit,
    onNavBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val foods = viewModel.foods.collectAsLazyPagingItems()

    ResultEffect<String> { code ->
        viewModel.onAction(UpsertRecipeAction.CodeScanned(code))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                UpsertRecipeEffect.NavBack -> onNavBack()
                UpsertRecipeEffect.LaunchCamera -> onLaunchCamera()
                UpsertRecipeEffect.ScanNotFound -> {}
            }
        }
    }

    val title = if (state.isCreate) R.string.create else R.string.edit

    when {
        state.isLoading -> FullScreenLoading()
        else -> Scaffold(
            topBar = { TopBar(title, onBackPressed = onNavBack) },
        ) { innerPadding ->
            UpsertRecipe(
                modifier = Modifier.padding(innerPadding),
                foods = foods,
                name = state.name,
                portions = state.portions,
                isSelectionMode = state.isSelectMode,
                selectedFoods = state.selectedFoods,
                isCreate = state.isCreate,
                onAction = viewModel::onAction
            )
        }
    }
}

@Composable
private fun UpsertRecipe(
    modifier: Modifier = Modifier,
    foods: LazyPagingItems<FoodEntity>,
    name: String,
    portions: Double?,
    isSelectionMode: Boolean,
    selectedFoods: Map<Int, RecipeFoodEntity>,
    isCreate: Boolean,
    onAction: (UpsertRecipeAction) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (isCreate) {
            focusRequester.requestFocus()
        }
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            FormTextField(
                modifier = Modifier.focusRequester(focusRequester),
                required = true,
                value = name,
                label = R.string.name
            ) {
                onAction(UpsertRecipeAction.SetName(it))
            }
        }

        items(items = selectedFoods.values.toList(), key = { it.foodId }) { recipeFood ->
            val position = SwipeToDismissBoxDefaults.positionalThreshold

            val swipeToDismissState = remember(recipeFood.foodId) {
                SwipeToDismissBoxState(
                    initialValue = SwipeToDismissBoxValue.Settled,
                    positionalThreshold = position
                )
            }

            SwipeToDismissBox(
                state = swipeToDismissState,
                enableDismissFromEndToStart = false,
                onDismiss = {
                    onAction(UpsertRecipeAction.ToggleSelection(recipeFood.food))
                },
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.error)
                    )
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    FoodCell(
                        modifier = Modifier.weight(1f),
                        item = recipeFood.food,
                        quantity = recipeFood.recipeQuantity
                    )

                    FormNumberField(
                        modifier = Modifier.weight(0.5f),
                        value = recipeFood.recipeQuantity,
                        label = stringResource(R.string.quantityUnitLabel, recipeFood.food.unit)
                    ) { input ->
                        onAction(
                            UpsertRecipeAction.SetRecipeFoodQuantity(recipeFood.food, input)
                        )
                    }
                }
            }
        }

        item {
            FoodSelectorButtons(
                onClickSelectButton = { onAction(UpsertRecipeAction.ToggleSelectMode) },
                onClickScanButton = { onAction(UpsertRecipeAction.LaunchCamera) }
            )

            FormNumberField(
                value = portions,
                label = R.string.portions,
                required = true,
                onValueChange = { onAction(UpsertRecipeAction.SetPortions(it)) }
            )

            val title = if (isCreate) R.string.create else R.string.edit

            ActionButton(modifier = Modifier.padding(vertical = 10.dp), text = title) {
                onAction(UpsertRecipeAction.Create)
            }
        }
    }

    if (isSelectionMode) {
        FoodSelection(foods, selectedFoods, onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodSelection(
    foods: LazyPagingItems<FoodEntity>,
    selectedFoods: Map<Int, RecipeFoodEntity>,
    onAction: (UpsertRecipeAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onAction(UpsertRecipeAction.ToggleSelectMode) },
        sheetState = sheetState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            FoodSelectionList(foods, selectedFoods.keys) {
                onAction(UpsertRecipeAction.ToggleSelection(it))
            }
        }
    }
}

@Composable
private fun FoodSelectionList(
    items: LazyPagingItems<FoodEntity>,
    selectedFoods: Set<Int>,
    onToggleSelection: (FoodEntity) -> Unit
) {
    if (items.loadState.isIdle && items.itemCount == 0) {
        NoData(
            icon = R.drawable.food,
            iconDescription = R.string.food,
            text = R.string.noFoods
        )
    } else {
        LazyColumn {
            items(count = items.itemCount, key = items.itemKey { it.id }) { index ->
                val item = items[index]
                if (item != null) {
                    FoodSelectionCell(item, selectedFoods) {
                        onToggleSelection(item)
                    }
                } else {
                    Text("Unavailable")
                }
            }
        }
    }
}

@Composable
private fun FoodSelectionCell(
    item: FoodEntity,
    selectedFoods: Set<Int>,
    toggleSelection: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { toggleSelection() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selectedFoods.contains(item.id),
            onCheckedChange = { toggleSelection() }
        )
        FoodCell(modifier = Modifier.fillMaxWidth(), item = item)
    }
}


@Preview
@Composable
private fun UpsertRecipePreview() {
    val item = FoodEntity(
        id = 1,
        name = "Chicken",
        calories = 5,
        quantity = 100.0,
        isRecipe = true,
        unit = FoodUnit.G
    )

    val items = flowOf(PagingData.from(listOf(item))).collectAsLazyPagingItems()

    FoodTrackerTheme {
        Surface {
            UpsertRecipe(
                foods = items,
                name = "",
                portions = 1.0,
                isSelectionMode = false,
                selectedFoods = mapOf(
                    1 to RecipeFoodEntity(
                        recipeId = 0,
                        foodId = 1,
                        food = item,
                        recipeQuantity = 10.0
                    )
                ),
                isCreate = false,
                onAction = {}
            )
        }
    }
}