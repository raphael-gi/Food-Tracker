package com.kenji.food.tracker.ui.screen.food

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.ui.Route
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.button.AddButton
import com.kenji.food.tracker.ui.component.cell.FoodCell
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.food.list.FoodListAction
import com.kenji.food.tracker.ui.viewmodel.food.list.FoodListViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun FoodListScreen(
    viewModel: FoodListViewModel = hiltViewModel(),
    onNavigate: (Route) -> Unit,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val items = viewModel.items.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopBar(
                title = R.string.foods,
                onBackPressed = onBackPressed,
                actions = {
                    AnimatedVisibility(state.selectedItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onAction(FoodListAction.DeleteSelected) }) {
                            Icon(
                                painter = painterResource(R.drawable.delete),
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            AddButton {
                onNavigate(Route.AddFood)
            }
        },
    ) { innerPadding ->
        FoodList(
            modifier = Modifier.padding(innerPadding),
            items = items,
            selectedItems = state.selectedItems,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun FoodList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<FoodEntity>,
    selectedItems: Set<Int>,
    onAction: (FoodListAction) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(count = items.itemCount, key = items.itemKey { it.id }) { index ->
            val item = items[index]
            if (item != null) {
                val background = if (item.id in selectedItems) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.background
                }

                FoodCell(
                    modifier = Modifier
                        .combinedClickable(
                            onLongClick = {
                                onAction(FoodListAction.ToggleSelection(item.id))
                            },
                            onClick = {}
                        )
                        .background(background),
                    item = item
                )
            } else {
                Text("Unavailable")
            }
        }
    }
}


@Preview(heightDp = 500, widthDp = 300)
@Composable
private fun PreviewFoodListScreen() {
    val items = flowOf(
        PagingData.from(
            listOf(
                FoodEntity(
                    id = 1,
                    name = "Chicken",
                    calories = 25,
                    protein = 20,
                    quantity = 100,
                    isRecipe = false,
                    unit = FoodUnit.G
                ),
                FoodEntity(
                    id = 2,
                    name = "Chicken 2",
                    calories = 25,
                    quantity = 100,
                    isRecipe = false,
                    unit = FoodUnit.G
                )
            )
        )
    ).collectAsLazyPagingItems()

    FoodTrackerTheme {
        Surface {
            FoodList(
                modifier = Modifier,
                items = items,
                selectedItems = emptySet()
            ) {}
        }
    }
}
