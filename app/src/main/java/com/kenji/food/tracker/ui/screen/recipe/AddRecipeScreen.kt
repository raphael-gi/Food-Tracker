package com.kenji.food.tracker.ui.screen.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.button.ActionButton
import com.kenji.food.tracker.ui.component.button.SelectionButton
import com.kenji.food.tracker.ui.component.cell.FoodCell
import com.kenji.food.tracker.ui.component.input.FormTextField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.recipe.add.AddRecipeAction
import com.kenji.food.tracker.ui.viewmodel.recipe.add.AddRecipeEffect
import com.kenji.food.tracker.ui.viewmodel.recipe.add.AddRecipeViewModel

@Composable
fun AddRecipeScreen(viewModel: AddRecipeViewModel = hiltViewModel(), onNavBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val foods = viewModel.foods.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddRecipeEffect.NavBack -> onNavBack()
            }
        }
    }

    Scaffold(
        topBar = { TopBar(R.string.add, onBackPressed = { onNavBack() }) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ActionButton(text = R.string.add) {
                viewModel.onAction(AddRecipeAction.Create)
            }
        }
    ) { innerPadding ->
        AddRecipe(
            modifier = Modifier.padding(innerPadding),
            name = state.name,
            foods = foods,
            isSelectionMode = state.isSelectMode,
            selectedFoods = state.selectedFoods,
            onAction = viewModel::onAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddRecipe(
    modifier: Modifier,
    foods: LazyPagingItems<FoodEntity>,
    name: String,
    isSelectionMode: Boolean,
    selectedFoods: Map<Int, FoodEntity>,
    onAction: (AddRecipeAction) -> Unit
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
            label = R.string.name
        ) {
            onAction(AddRecipeAction.SetName(it))
        }

        LazyColumn {
            items(items = selectedFoods.values.toList(), key = { it.id }) { food ->
                val position = SwipeToDismissBoxDefaults.positionalThreshold

                val swipeToDismissState = remember(food.id) {
                    SwipeToDismissBoxState(
                        initialValue = SwipeToDismissBoxValue.Settled,
                        positionalThreshold = position
                    )
                }

                SwipeToDismissBox(
                    state = swipeToDismissState,
                    enableDismissFromEndToStart = false,
                    onDismiss = {
                        onAction(AddRecipeAction.ToggleSelection(food))
                    },
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.error)
                        )
                    }
                ) {
                    FoodCell(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        item = food
                    )
                }
            }
        }

        SelectionButton(text = R.string.selectFood) {
            onAction(AddRecipeAction.ToggleSelectMode)
        }

        if (isSelectionMode) {
            FoodSelection(foods, selectedFoods, onAction)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodSelection(
    foods: LazyPagingItems<FoodEntity>,
    selectedFoods: Map<Int, FoodEntity>,
    onAction: (AddRecipeAction) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onAction(AddRecipeAction.ToggleSelectMode) },
        sheetState = sheetState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            FoodSelectionList(foods, selectedFoods.keys) {
                onAction(AddRecipeAction.ToggleSelection(it))
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
        FoodCell(item = item)
    }
}


@Preview
@Composable
private fun AddRecipePreview() {
    FoodTrackerTheme {
        Surface {
            // AddRecipe(Modifier, "") { }
        }
    }
}
