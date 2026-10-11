package com.kenji.food.tracker.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.entity.FoodTargetEntity
import com.kenji.food.tracker.ui.component.cell.CountedMealCell

@Composable
fun FoodPerDayOverview(countedMeals: List<CountedMealEntity>, target: FoodTargetEntity) {
    LazyColumn {
        stickyHeader {
        }
        items(countedMeals) { countedMeal ->
            CountedMealCell(item = countedMeal)
        }
    }
}