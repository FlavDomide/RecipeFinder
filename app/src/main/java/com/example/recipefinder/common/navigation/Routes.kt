package com.example.recipefinder.common.navigation

import android.net.Uri

sealed class Routes(val name: String) {
    data object RecipeHome : Routes("recipe_home")

    data object RecipeDetails :
        Routes("recipe_details/{title}/{url}/{ingredients}/{instructions}") {
        fun passArguments(
            title: String,
            url: String,
            ingredients: String,
            instructions: String
        ): String {
            return "recipe_details/${Uri.encode(title)}/${Uri.encode(url)}/${Uri.encode(ingredients)}/${
                Uri.encode(
                    instructions
                )
            }"
        }
    }
}