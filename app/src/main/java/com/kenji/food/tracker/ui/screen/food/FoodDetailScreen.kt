package com.kenji.food.tracker.ui.screen.food

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.ui.component.FullScreenLoading
import com.kenji.food.tracker.ui.component.button.EditButton
import com.kenji.food.tracker.ui.component.button.NavigationButton
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.theme.carbs
import com.kenji.food.tracker.ui.theme.fats
import com.kenji.food.tracker.ui.theme.protein
import com.kenji.food.tracker.ui.theme.saturatedFats
import com.kenji.food.tracker.ui.theme.sugar
import com.kenji.food.tracker.ui.viewmodel.food.detail.FoodDetailViewModel
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.pie.PieChart
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.pieSeries
import com.patrykandpatrick.vico.compose.pie.rememberPieChart

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
    val chartLabel = PieChart.SliceLabel.Inside(TextComponent(TextStyle(Color.White)))

    Box(modifier = modifier) {
        val slices = listOfNotNull(
            food.protein?.let {
                PieChart.Slice(
                    fill = Fill(MaterialTheme.colorScheme.protein),
                    label = chartLabel,
                )
            },
            food.carbs?.let {
                PieChart.Slice(
                    fill = Fill(MaterialTheme.colorScheme.carbs),
                    label = chartLabel,
                )
            },
            food.sugar?.let {
                PieChart.Slice(
                    fill = Fill(MaterialTheme.colorScheme.sugar),
                    label = chartLabel,
                )
            },
            food.fats?.let {
                PieChart.Slice(
                    fill = Fill(MaterialTheme.colorScheme.fats),
                    label = chartLabel,
                )
            },
            food.saturatedFats?.let {
                PieChart.Slice(
                    fill = Fill(MaterialTheme.colorScheme.saturatedFats),
                    label = chartLabel,
                )
            },
        )

        if (slices.isNotEmpty()) {
            val chart = rememberPieChart(
                sliceProvider = PieChart.SliceProvider.series(slices)
            )

            val modelProducer = remember { PieChartModelProducer() }
            LaunchedEffect(Unit) {
                modelProducer.runTransaction {
                    pieSeries {
                        series(
                            listOfNotNull(
                                food.protein,
                                food.carbs,
                                food.sugar,
                                food.fats,
                                food.saturatedFats
                            )
                        )
                    }
                }
            }

            PieChartHost(
                chart = chart,
                modelProducer = modelProducer
            )
        } else {
            Text("No Macros")
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
        carbs = 20.0,
        protein = 50.0,
        fats = 10.0,
        isRecipe = false,
        unit = FoodUnit.G,
        quantity = 100.0
    )

    FoodTrackerTheme {
        Surface {
            FoodDetailContent(food = food)
        }
    }
}
