package com.example.recipefinder.features.homescreen.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipefinder.R
import com.example.recipefinder.common.navigation.Routes
import com.example.recipefinder.features.homescreen.data.models.RecipeHomeModel
import com.example.recipefinder.features.homescreen.viewmodels.RecipeHomeViewModel

@Composable
fun RecipeHomeScreen(
    navController: NavController,
    recipeHomeViewModel: RecipeHomeViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    val favorites by recipeHomeViewModel.favorites.collectAsState()
    val recipes by recipeHomeViewModel.recipes.collectAsState()
    val isLoading by recipeHomeViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query,
            onQueryChange = { query = it },
            onSearch = { recipeHomeViewModel.fetchRecipes(query) })
        Spacer(modifier = Modifier.height(16.dp))

        Text("Favorites", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        LazyColumn {
            items(favorites) { recipe ->
                RecipeItem(
                    recipe,
                    navController,
                    onFavoriteClick = { recipeHomeViewModel.toggleFavorite(recipe) },
                    isFavorite = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                "Suggested Recipes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            LazyColumn {
                items(recipes) { recipe ->
                    RecipeItem(
                        recipe,
                        navController,
                        onFavoriteClick = { recipeHomeViewModel.toggleFavorite(recipe) },
                        isFavorite = false
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { recipeHomeViewModel.fetchRecipes(query) },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.purple_700))
                ) {
                    Text("I don’t like these")
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("What do you feel like eating?") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onSearch) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
    }
}

@Composable
fun RecipeItem(
    recipeHomeModel: RecipeHomeModel,
    navController: NavController,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    recipeHomeModel
                        .url
                ),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .size(80.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navController.navigate(
                            Routes.RecipeDetails.passArguments(
                                recipeHomeModel.title,
                                recipeHomeModel.url,
                                recipeHomeModel.ingredients ?: "",
                                recipeHomeModel.instructions ?: ""
                            )
                        )
                    }) {
                Text(
                    recipeHomeModel.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Text(recipeHomeModel.duration)
            }
            IconButton(onClick = onFavoriteClick, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}