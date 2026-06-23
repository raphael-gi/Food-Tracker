package com.kenji.food.tracker.ui.screen.recipe

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.ui.Route
import com.kenji.food.tracker.ui.component.RecipeCell
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.recipe.list.RecipeListViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onNavigate: (Route) -> Unit,
    onBackPressed: () -> Unit
) {
    val items = viewModel.items.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopBar(title = R.string.recipes, onBackPressed = onBackPressed) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate(Route.AddRecipe) },
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { innerPadding ->
        RecipeList(Modifier.padding(innerPadding), items = items)
    }
}

@Composable
private fun RecipeList(modifier: Modifier = Modifier, items: LazyPagingItems<Recipe>) {
    LazyColumn(modifier = modifier) {
        items(count = items.itemCount, key = items.itemKey { it.food.id }) { index ->
            val item = items[index]
            if (item != null) {
                RecipeCell(item = item)
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
                FoodEntity(
                    id = 1, name = "Chicken",
                    calories = 5,
                    quantity = 100,
                    isRecipe = true,
                    unit = FoodUnit.G
                ),
            )
        )
    ).collectAsLazyPagingItems()

    FoodTrackerTheme {
        Surface {
            // RecipeList(modifier = Modifier, items = items)
        }
    }
}
