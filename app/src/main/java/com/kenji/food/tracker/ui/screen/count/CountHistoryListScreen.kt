package com.kenji.food.tracker.ui.screen.count

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.CountedMealEntity
import com.kenji.food.tracker.ui.component.TopBar
import com.kenji.food.tracker.ui.component.button.DeleteButton
import com.kenji.food.tracker.ui.component.cell.CountedMealCell
import com.kenji.food.tracker.ui.viewmodel.count.history.CountHistoryAction
import com.kenji.food.tracker.ui.viewmodel.count.history.CountHistoryListViewModel

@Composable
fun CountHistoryListScreen(viewModel: CountHistoryListViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val countedMeals = viewModel.items.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopBar(
                title = R.string.history,
                actions = {
                    AnimatedVisibility(state.selectedItems.isNotEmpty()) {
                        DeleteButton {
                            viewModel.onAction(CountHistoryAction.DeleteSelected)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        CountedMealList(
            modifier = Modifier.padding(innerPadding),
            items = countedMeals,
            selectedItems = state.selectedItems,
            onAction = viewModel::onAction
        )
    }
}

@Composable
private fun CountedMealList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<CountedMealEntity>,
    selectedItems: Set<Int>,
    onAction: (CountHistoryAction) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(count = items.itemCount, key = items.itemKey { it.id }) { index ->
            val item = items[index]
            if (item != null) {
                val background = if (item.id in selectedItems) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.background
                }

                CountedMealCell(
                    modifier = Modifier
                        .animateItem()
                        .background(background)
                        .combinedClickable(
                            onLongClick = {
                                onAction(CountHistoryAction.ToggleSelection(item.id))
                            },
                            onClick = {}
                        ),
                    item = item
                )
            } else {
                Text("Unavailable")
            }
        }
    }
}
