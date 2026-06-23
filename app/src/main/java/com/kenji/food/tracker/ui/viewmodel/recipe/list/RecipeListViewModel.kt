package com.kenji.food.tracker.ui.viewmodel.recipe.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kenji.food.tracker.db.dao.FoodDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {
    private val _state = MutableStateFlow(
        RecipeListState()
    )
    val state = _state.asStateFlow()

    val items = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { foodDao.getAllRecipes() }
    ).flow.cachedIn(viewModelScope)

    fun onAction(action: RecipeListAction) {
        when (action) {
            else -> {}
        }
    }
}