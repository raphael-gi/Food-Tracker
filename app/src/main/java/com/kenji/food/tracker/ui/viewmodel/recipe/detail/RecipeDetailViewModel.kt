package com.kenji.food.tracker.ui.viewmodel.recipe.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenji.food.tracker.db.dao.FoodDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = RecipeDetailViewModel.Factory::class)
class RecipeDetailViewModel @AssistedInject constructor(
    foodDao: FoodDao,
    @Assisted private val id: Int
) : ViewModel() {
    private val _state = MutableStateFlow(RecipeDetailState())
    val state = _state.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(id: Int): RecipeDetailViewModel
    }

    init {
        viewModelScope.launch {
            foodDao.getRecipeById(id).collect { recipe ->
                _state.update { it.copy(recipe = recipe) }
            }
        }
    }
}