package com.kenji.food.tracker.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.entity.FoodEntity
import com.kenji.food.tracker.entity.Recipe

@Composable
fun RecipeCell(modifier: Modifier = Modifier, item: Recipe) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = item.food.name,
            style = MaterialTheme.typography.titleMedium
        )
        RecipeTags(item.foods)
    }
}

@Composable
private fun RecipeTags(foods: List<FoodEntity>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        items(foods) { food ->
            Tag(text = food.name)
        }
    }
}
