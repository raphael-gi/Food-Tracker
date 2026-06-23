package com.kenji.food.tracker.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.kenji.food.tracker.ui.Route
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.ui.viewmodel.HomeViewModel

private val SPACING = 10.dp

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onNavigate: (Route) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        caloriesEatenToday = state.caloriesEatenToday.toString(),
        onNavigate = onNavigate
    )
}

@Composable
private fun HomeContent(
    caloriesEatenToday: String,
    onNavigate: (Route) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SPACING),
        verticalArrangement = Arrangement.spacedBy(SPACING)
    ) {
        HomeTile(
            modifier = Modifier.height(100.dp),
            onClick = { onNavigate(Route.Count) }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                text = "Count Food"
            )
        }

        HomeTile(modifier = Modifier.weight(2.5f), onClick = {}) {
            Text(caloriesEatenToday)
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
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(),
        content = content
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    FoodTrackerTheme {
        Surface {
            HomeContent(caloriesEatenToday = "5") { }
        }
    }
}