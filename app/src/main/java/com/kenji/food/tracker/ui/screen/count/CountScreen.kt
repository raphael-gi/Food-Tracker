package com.kenji.food.tracker.ui.screen.count

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.ui.component.ActionButton
import com.kenji.food.tracker.ui.component.FoodCell
import com.kenji.food.tracker.ui.component.RecipeCell
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.viewmodel.count.CountAction
import com.kenji.food.tracker.ui.viewmodel.count.CountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountScreen(viewModel: CountViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val foods = viewModel.meals.collectAsLazyPagingItems()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = { TopBar(title = R.string.countMeal) },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ActionButton(text = R.string.confirm) {
                viewModel.onAction(CountAction.CountMeal)
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            state.selectedMeal?.let { selectedMeal ->
                Text(selectedMeal.food.name)
            }

            Button(onClick = { viewModel.onAction(CountAction.ToggleSelectMode) }) {
                Text("Select Food")
            }

            if (state.isSelectMode) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onAction(CountAction.ToggleSelectMode) },
                    sheetState = sheetState
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SelectionList(foods) {
                            viewModel.onAction(CountAction.SelectMeal(it))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectionList(items: LazyPagingItems<Recipe>, onSelect: (Recipe) -> Unit) {
    LazyColumn {
        items(count = items.itemCount, key = items.itemKey { it.food.id }) { index ->
            val item = items[index]
            if (item != null) {
                FoodSelectionCell(item) {
                    onSelect(item)
                }
            } else {
                Text("Unavailable")
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
            modifier = Modifier.clickable { onSelect() },
            item = item.food
        )
    }
}
