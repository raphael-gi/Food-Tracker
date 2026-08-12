package com.kenji.food.tracker.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodPerDay
import com.kenji.food.tracker.entity.FoodTargetEntity
import com.kenji.food.tracker.ui.Route
import com.kenji.food.tracker.ui.component.chart.WeekOverviewChart
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.HomeViewModel

private val SPACING = 10.dp

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onNavigate: (Route) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        foodPerDay = state.foodPerDays,
        currentTarget = state.currentTarget,
        onNavigate = onNavigate
    )
}

@Composable
private fun HomeContent(
    foodPerDay: List<FoodPerDay>,
    currentTarget: FoodTargetEntity?,
    onNavigate: (Route) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SPACING),
        verticalArrangement = Arrangement.spacedBy(SPACING)
    ) {
        HomeTile(modifier = Modifier.weight(2.5f)) {
            currentTarget?.let { target ->
                val items = buildList {
                    add(@Composable {
                        WeekOverviewChart(
                            foodPerDay = foodPerDay,
                            getKey = FoodPerDay::calories,
                            title = R.string.calories,
                            currentTarget = target.calories,
                            valuePadding = 200
                        )
                    })
                    if (target.protein != null) {
                        add(@Composable {
                            WeekOverviewChart(
                                foodPerDay = foodPerDay,
                                getKey = FoodPerDay::protein,
                                title = R.string.protein,
                                currentTarget = target.protein,
                                valuePadding = 20
                            )
                        })
                    }
                    if (target.sugar != null) {
                        add(@Composable {
                            WeekOverviewChart(
                                foodPerDay = foodPerDay,
                                getKey = FoodPerDay::sugar,
                                title = R.string.sugar,
                                currentTarget = target.sugar,
                                valuePadding = 10
                            )
                        })
                    }
                }

                val pagerState = rememberPagerState { items.size }

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = SPACING,
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        snapPositionalThreshold = 0.2f
                    ),
                    state = pagerState
                ) { pageIndex ->
                    items[pageIndex]()
                }

            }
        }

        HomeTile(
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(Route.FoodList) }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                text = stringResource(R.string.foods),
            )
        }

        HomeTile(
            modifier = Modifier.weight(1f),
            onClick = { onNavigate(Route.RecipeList) }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                text = stringResource(R.string.recipes)
            )
        }
    }
}

@Composable
private fun HomeTile(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(),
        shape = ShapeDefaults.Small,
        content = content
    )
}


@Preview
@Composable
private fun HomeScreenPreview() {
    val target = FoodTargetEntity(
        id = 0,
        calories = 2000,
        protein = null,
        sugar = null
    )

    FoodTrackerTheme {
        Surface {
            HomeContent(foodPerDay = emptyList(), currentTarget = target) { }
        }
    }
}