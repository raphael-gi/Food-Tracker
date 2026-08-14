package com.kenji.food.tracker.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.util.Formatter

private const val SPACING = 10

@OptIn(ExperimentalGridApi::class)
@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
    name: String,
    calories: Int?,
    protein: Double?,
    carbs: Double?,
    sugar: Double?,
    fats: Double?,
    saturatedFats: Double?,
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(SPACING.dp)
        ) {
            calories?.let {
                MacroCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.totalKcal, it))
                        Icon(
                            painter = painterResource(R.drawable.calories),
                            contentDescription = stringResource(R.string.calories)
                        )
                    }
                }
            }

            protein?.let {
                MacroCard(modifier = Modifier.fillMaxWidth()) {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(R.drawable.protein),
                            contentDescription = stringResource(R.string.protein)
                        )
                    }
                    Text("${Formatter.formatDecimal(it)}g")
                    Text(stringResource(R.string.protein))
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(SPACING.dp)) {
                carbs?.let {
                    MacroCard(modifier = Modifier.weight(1f)) {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier,
                                painter = painterResource(R.drawable.carbs),
                                contentDescription = stringResource(R.string.carbs)
                            )
                        }
                        Text("${Formatter.formatDecimal(it)}g")
                        Text(stringResource(R.string.carbs))
                    }
                }
                sugar?.let {
                    MacroCard(modifier = Modifier.weight(1f)) {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier,
                                painter = painterResource(R.drawable.sugar),
                                contentDescription = stringResource(R.string.sugar)
                            )
                        }
                        Text("${Formatter.formatDecimal(it)}g")
                        Text(stringResource(R.string.sugar))
                    }
                }
            }

            Row {
                fats?.let {
                    MacroCard(modifier = Modifier.weight(1f)) {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier,
                                painter = painterResource(R.drawable.fat),
                                contentDescription = stringResource(R.string.fat)
                            )
                        }
                        Text("${Formatter.formatDecimal(it)}g")
                        Text(stringResource(R.string.fat))
                    }
                }
                saturatedFats?.let {
                    MacroCard(modifier = Modifier.weight(1f)) {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier,
                                painter = painterResource(R.drawable.saturatedfat),
                                contentDescription = stringResource(R.string.saturatedFat)
                            )
                        }
                        Text("${Formatter.formatDecimal(it)}g")
                        Text(stringResource(R.string.saturatedFat))
                    }
                }
            }
        }
    }
}

@Composable
private fun MacroCard(modifier: Modifier, content: @Composable (ColumnScope.() -> Unit)) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.elevatedCardColors(),
        shape = ShapeDefaults.Small,
        content = {
            Column(
                modifier = Modifier.padding(10.dp),
                content = content
            )
        }
    )
}


@Preview
@Composable
private fun FoodDetailPreview() {
    FoodTrackerTheme {
        Surface {
            FoodCard(
                name = "Banana",
                calories = 100,
                carbs = 20.0,
                sugar = 5.0,
                protein = 50.0,
                fats = 10.0,
                saturatedFats = null
            )
        }
    }
}
