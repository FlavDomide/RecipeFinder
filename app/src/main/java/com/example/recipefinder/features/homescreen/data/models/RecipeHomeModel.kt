package com.example.recipefinder.features.homescreen.data.models

data class RecipeHomeModel(
    val title: String,
    val ingredients: String? = "",
    val instructions: String? = "",
    val url: String = "",
    val duration: String = "20 min"
)
