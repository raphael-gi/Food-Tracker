package com.kenji.food.tracker.ui.screen.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.kenji.food.tracker.ui.component.FullScreenLoading
import com.kenji.food.tracker.ui.component.button.EditButton
import com.kenji.food.tracker.ui.component.button.NavigationButton
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.food.detail.FoodDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    viewModel: FoodDetailViewModel,
    onPressEdit: () -> Unit,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val food = state.food) {
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
                            Text(food.name)
                        },
                        actions = {
                            EditButton(onPressEdit)
                        }
                    )
                }
            ) { innerPadding ->
                FoodDetailContent(
                    modifier = Modifier.padding(innerPadding),
                    food = food
                )
            }
        }
    }
}

@Composable
private fun FoodDetailContent(modifier: Modifier = Modifier, food: FoodEntity) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            food.calories?.let {
                Text(it.toString())
            }
            food.protein?.let {
                Text(it.toString())
            }
            food.carbs?.let {
                Text(it.toString())
            }
            food.fats?.let {
                Text(it.toString())
            }
        }
    }
}


@Preview
@Composable
private fun FoodDetailPreview() {
    val food = FoodEntity(
        id = 0,
        name = "Chicken",
        calories = 100,
        isRecipe = false,
        unit = FoodUnit.G,
        quantity = 100
    )

    FoodTrackerTheme {
        Surface {
            FoodDetailContent(food = food)
        }
    }
}
