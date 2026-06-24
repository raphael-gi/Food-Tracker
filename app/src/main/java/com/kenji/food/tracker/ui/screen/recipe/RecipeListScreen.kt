package com.kenji.food.tracker.ui.screen.recipe

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
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.ui.Route
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.button.AddButton
import com.kenji.food.tracker.ui.component.cell.RecipeCell
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.recipe.list.RecipeListAction
import com.kenji.food.tracker.ui.viewmodel.recipe.list.RecipeListViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onNavigate: (Route) -> Unit,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val items = viewModel.items.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopBar(
                title = R.string.recipes,
                onBackPressed = onBackPressed,
                actions = {
                    AnimatedVisibility(state.selectedItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onAction(RecipeListAction.DeleteSelected) }) {
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
                onNavigate(Route.AddRecipe)
            }
        }
    ) { innerPadding ->
        RecipeList(
            modifier = Modifier.padding(innerPadding),
            items = items,
            selectedItems = state.selectedItems,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun RecipeList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Recipe>,
    selectedItems: Set<Int>,
    onAction: (RecipeListAction) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(count = items.itemCount, key = items.itemKey { it.food.id }) { index ->
            val item = items[index]
            if (item != null) {
                val background = if (item.food.id in selectedItems) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.background
                }

                RecipeCell(
                    modifier = Modifier
                        .animateItem()
                        .background(background)
                        .combinedClickable(
                            onLongClick = {
                                onAction(RecipeListAction.ToggleSelection(item.food.id))
                            },
                            onClick = {}
                        ),
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
private fun PreviewRecipeListScreen() {
    val items = flowOf(
        PagingData.from(
            listOf(
                Recipe(
                    food = FoodEntity(
                        id = 1,
                        name = "Chicken",
                        calories = 5,
                        quantity = 100,
                        isRecipe = true,
                        unit = FoodUnit.G
                    ),
                    foods = emptyList()
                ),
            )
        )
    ).collectAsLazyPagingItems()

    FoodTrackerTheme {
        Surface {
            RecipeList(items = items, selectedItems = setOf(1)) {}
        }
    }
}
