package com.kenji.food.tracker.ui.viewmodel.food.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kenji.food.tracker.db.dao.FoodDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {
    private val _state = MutableStateFlow(FoodListState())
    val state = _state.asStateFlow()

    val items = Pager(
        config = PagingConfig(
            pageSize = 5,
        ),
        pagingSourceFactory = { foodDao.getAllFoods() }
    ).flow.cachedIn(viewModelScope)

    fun onAction(action: FoodListAction) {
        when (action) {
            FoodListAction.NextPage -> {}
            is FoodListAction.ToggleSelection -> this.onToggleSelection(action.id)
            FoodListAction.DeleteSelected -> this.onDeleteSelected()
        }
    }

    private fun onToggleSelection(id: Int) {
        _state.update {
            val newItems = if (it.selectedItems.contains(id)) {
                it.selectedItems - id
            } else {
                it.selectedItems + id
            }

            it.copy(selectedItems = newItems)
        }
    }

    private fun onDeleteSelected() {
        val selectedIds = state.value.selectedItems

        viewModelScope.launch {
            foodDao.delete(selectedIds)
            _state.update { it.copy(selectedItems = emptySet()) }
        }
    }
}