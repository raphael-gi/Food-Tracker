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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {
    private val _state = MutableStateFlow(
        FoodListState()
    )
    val state = _state.asStateFlow()

    val items = Pager(
        config = PagingConfig(
            pageSize = 5,
        ),
        pagingSourceFactory = { foodDao.getAllFoods() }
    ).flow.cachedIn(viewModelScope)

    init {
        loadData()
    }

    fun onAction(action: FoodListAction) {
        when (action) {
            else -> {}
        }
    }

    private fun loadData() {
        viewModelScope.launch {
        }
    }
}