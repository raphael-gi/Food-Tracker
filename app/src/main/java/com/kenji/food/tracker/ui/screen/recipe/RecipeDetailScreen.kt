package com.kenji.food.tracker.ui.screen.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.entity.Recipe
import com.kenji.food.tracker.entity.RecipeFoodEntity
import com.kenji.food.tracker.ui.component.FullScreenLoading
import com.kenji.food.tracker.ui.component.button.EditButton
import com.kenji.food.tracker.ui.component.button.NavigationButton
import com.kenji.food.tracker.ui.component.cell.FoodCell
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.recipe.detail.RecipeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel,
    onPressEdit: () -> Unit,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val recipe = state.recipe) {
        null -> {
            FullScreenLoading()
        }

        else -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            NavigationButton(onBackPressed)
                        },
                        title = {
                            Text(recipe.food.name)
                        },
                        actions = {
                            EditButton(onPressEdit)
                        }
                    )
                }
            ) { innerPadding ->
                FoodDetailContent(
                    modifier = Modifier.padding(innerPadding),
                    recipe = recipe
                )
            }
        }
    }
}

@Composable
private fun FoodDetailContent(modifier: Modifier = Modifier, recipe: Recipe) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(state = scrollState)) {
        recipe.foods.forEach { recipeFood ->
            FoodCell(item = recipeFood.food, quantity = recipeFood.recipeQuantity)
        }
    }
}


@Preview
@Composable
private fun FoodDetailPreview() {
    val food = Recipe(
        food = FoodEntity(
            id = 0,
            name = "Chicken",
            calories = 0,
            isRecipe = true,
            unit = FoodUnit.G,
            quantity = 100.0
        ),
        foods = listOf(
            RecipeFoodEntity(
                recipeId = 0,
                foodId = 1,
                food = FoodEntity(
                    id = 1,
                    name = "Chicken",
                    calories = 100,
                    isRecipe = false,
                    unit = FoodUnit.G,
                    quantity = 100.0
                ),
                recipeQuantity = 10.0
            )
        )
    )

    FoodTrackerTheme {
        Surface {
            FoodDetailContent(recipe = food)
        }
    }
}
