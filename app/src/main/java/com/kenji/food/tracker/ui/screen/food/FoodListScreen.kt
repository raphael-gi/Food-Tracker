package com.kenji.food.tracker.ui.screen.food

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.kenji.food.tracker.ui.component.button.AddButton
import com.kenji.food.tracker.ui.component.button.DeleteButton
import com.kenji.food.tracker.ui.component.button.NavigationButton
import com.kenji.food.tracker.ui.component.cell.FoodCell
import com.kenji.food.tracker.ui.component.info.NoData
import com.kenji.food.tracker.ui.component.input.SearchField
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.food.list.FoodListAction
import com.kenji.food.tracker.ui.viewmodel.food.list.FoodListEffect
import com.kenji.food.tracker.ui.viewmodel.food.list.FoodListViewModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(
    viewModel: FoodListViewModel = hiltViewModel(),
    onNavigate: (Route) -> Unit,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val items = viewModel.items.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FoodListEffect.ItemSelected -> onNavigate(Route.FoodDetail(effect.item.id))
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = { NavigationButton(onBackPressed) },
                title = {
                    SearchField(query = state.query) {
                        viewModel.onAction(FoodListAction.Search(it))
                    }
                },
                actions = {
                    AnimatedVisibility(state.selectedItems.isNotEmpty()) {
                        DeleteButton {
                            viewModel.onAction(FoodListAction.DeleteSelected)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            AddButton {
                onNavigate(Route.UpsertFood())
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<FoodEntity>,
    selectedItems: Set<Int>,
    onAction: (FoodListAction) -> Unit
) {
    if (items.loadState.isIdle && items.itemCount == 0) {
        NoData(
            icon = R.drawable.food,
            iconDescription = R.string.food,
            text = R.string.noFoods
        )
    } else {
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
                            .fillMaxWidth()
                            .animateItem()
                            .background(background)
                            .combinedClickable(
                                onLongClick = {
                                    onAction(FoodListAction.ToggleSelection(item.id))
                                },
                                onClick = {
                                    onAction(FoodListAction.SelectItem(item))
                                }
                            ),
                        item = item
                    )
                } else {
                    Text("Unavailable")
                }
            }
        }
    }
}


@Preview(heightDp = 500, widthDp = 300)
@Composable
private fun PreviewFoodListScreen() {
    val items = flowOf(
        PagingData.from(
            (0..10).map {
                FoodEntity(
                    id = it,
                    name = "Chicken",
                    calories = 25,
                    protein = 20.0,
                    quantity = 100.0,
                    isRecipe = false,
                    unit = FoodUnit.G
                )
            },
        )
    ).collectAsLazyPagingItems()

    FoodTrackerTheme {
        Surface {
            FoodList(
                items = items,
                selectedItems = emptySet()
            ) {}
        }
    }
}
