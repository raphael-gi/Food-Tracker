package com.kenji.food.tracker.ui.viewmodel.count.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kenji.food.tracker.db.dao.CountedMealDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountHistoryListViewModel @Inject constructor(
    private val countedMealDao: CountedMealDao
) : ViewModel() {
    private val _state = MutableStateFlow(CountHistoryState())
    val state = _state.asStateFlow()

    fun onAction(action: CountHistoryAction) {
        when (action) {
            is CountHistoryAction.ToggleSelection -> this.onToggleSelection(action.id)
            CountHistoryAction.DeleteSelected -> this.onDeleteSelected()
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
        val ids = state.value.selectedItems

        viewModelScope.launch {
            countedMealDao.delete(ids)
        }
    }

    val items = Pager(
        config = PagingConfig(
            pageSize = 5,
        ),
        pagingSourceFactory = { countedMealDao.getAll() }
    ).flow.cachedIn(viewModelScope)
}