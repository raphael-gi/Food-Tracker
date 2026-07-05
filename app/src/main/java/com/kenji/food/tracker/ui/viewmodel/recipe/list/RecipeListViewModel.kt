package com.kenji.food.tracker.ui.viewmodel.recipe.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kenji.food.tracker.db.dao.FoodDao
import com.kenji.food.tracker.entity.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {
    private val _state = MutableStateFlow(RecipeListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<RecipeListEffect>()
    val effect = _effect.receiveAsFlow()

    val items = Pager(
        config = PagingConfig(pageSize = 5),
        pagingSourceFactory = { foodDao.getAllRecipes() }
    ).flow.cachedIn(viewModelScope)

    fun onAction(action: RecipeListAction) {
        when (action) {
            RecipeListAction.NextPage -> {}
            is RecipeListAction.ToggleSelection -> this.onToggleSelection(action.id)
            RecipeListAction.DeleteSelected -> this.onDeleteSelected()
            is RecipeListAction.SelectItem -> this.onSelectItem(action.item)
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

    private fun onSelectItem(item: Recipe) {
        viewModelScope.launch {
            _effect.send(RecipeListEffect.ItemSelected(item))
        }
    }
}