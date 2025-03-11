package com.example.recipefinder.features.homescreen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.features.homescreen.data.models.RecipeHomeModel
import com.example.recipefinder.features.homescreen.data.repositories.GetRecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeHomeViewModel @Inject constructor(private val getRecipesRepository: GetRecipesRepository) :
    ViewModel() {
    private val _recipes = MutableStateFlow<List<RecipeHomeModel>>(emptyList())
    val recipes: StateFlow<List<RecipeHomeModel>> = _recipes
    private val _favorites = MutableStateFlow<List<RecipeHomeModel>>(emptyList())
    val favorites: StateFlow<List<RecipeHomeModel>> = _favorites

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchRecipes(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            getRecipesRepository.getRecipes(query).collectLatest { recipes ->
                _recipes.value = recipes
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(recipe: RecipeHomeModel) {
        _favorites.value = if (_favorites.value.contains(recipe)) {
            _favorites.value - recipe
        } else {
            _favorites.value + recipe
        }
    }
}