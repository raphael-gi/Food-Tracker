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
import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.ui.component.Tag
import com.kenji.food.tracker.ui.theme.FoodTrackerTheme
import com.kenji.food.tracker.util.Formatter
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun CountedMealCell(modifier: Modifier = Modifier, item: CountedMealEntity) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )

            val date = Date(item.eatenAt).toInstant()
            val format = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault())

            Text(format.format(date))
        }
        FoodTags(item)
    }
}

@Composable
private fun FoodTags(item: CountedMealEntity) {
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
    val item = CountedMealEntity(
        id = 1,
        name = "Chicken",
        calories = 25,
        protein = 20.0,
    )

    FoodTrackerTheme {
        Surface {
            CountedMealCell(item = item)
        }
    }
}
