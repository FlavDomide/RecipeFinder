package com.example.recipefinder.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipefinder.features.details.views.RecipeDetailsScreen
import com.example.recipefinder.features.homescreen.views.RecipeHomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.RecipeHome.name,
        modifier = modifier
    ) {
        composable(Routes.RecipeHome.name) { RecipeHomeScreen(navController) }
        composable(
            route = Routes.RecipeDetails.name, arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType },
                navArgument("ingredients") { type = NavType.StringType },
                navArgument("instructions") { type = NavType.StringType }
            )) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val url = backStackEntry.arguments?.getString("url")
            val ingredients = backStackEntry.arguments?.getString("ingredients")
            val instructions = backStackEntry.arguments?.getString("instructions")
            RecipeDetailsScreen(
                navController, title, url, ingredients, instructions
            )
        }
    }
}