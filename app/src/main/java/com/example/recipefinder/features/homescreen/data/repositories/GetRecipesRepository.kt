package com.example.recipefinder.features.homescreen.data.repositories

import com.example.recipefinder.features.homescreen.data.models.RecipeHomeModel
import kotlinx.coroutines.flow.Flow

interface
GetRecipesRepository {
    fun getRecipes(query: String): Flow<List<RecipeHomeModel>>
}