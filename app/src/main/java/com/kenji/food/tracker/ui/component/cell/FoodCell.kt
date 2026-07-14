package com.kenji.food.tracker.ui.component.cell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.FoodUnit
import com.kenji.food.tracker.ui.component.Tag
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.util.Formatter

@Composable
fun FoodCell(modifier: Modifier = Modifier, item: FoodEntity) {
    Column(
        modifier = modifier.padding(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${Formatter.formatDecimal(item.quantity)}${item.unit.name.lowercase()}",
                style = MaterialTheme.typography.labelMedium
            )
        }
        FoodTags(item)
    }
}

@Composable
private fun FoodTags(item: FoodEntity) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Tag(text = item.calories.toString())
        item.carbs?.let {
            Tag(text = Formatter.formatDecimal(it))
        }
        item.protein?.let {
            Tag(text = Formatter.formatDecimal(it))
        }
        item.fats?.let {
            Tag(text = Formatter.formatDecimal(it))
        }
    }
}


@Preview(heightDp = 500, widthDp = 300)
@Composable
private fun PreviewFoodListScreen() {
    val item = FoodEntity(
        id = 1,
        name = "Chicken",
        calories = 25,
        protein = 20.0,
        quantity = 100.0,
        isRecipe = false,
        unit = FoodUnit.G
    )

    FoodTrackerTheme {
        Surface {
            FoodCell(item = item)
        }
    }
}
