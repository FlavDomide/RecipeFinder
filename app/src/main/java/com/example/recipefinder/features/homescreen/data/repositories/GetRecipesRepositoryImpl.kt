package com.example.recipefinder.features.homescreen.data.repositories

import com.example.recipefinder.features.homescreen.data.datasource.Message
import com.example.recipefinder.features.homescreen.data.datasource.OpenAIResponse
import com.example.recipefinder.features.homescreen.data.datasource.OpenAiApi
import com.example.recipefinder.features.homescreen.data.datasource.OpenAiRequest
import com.example.recipefinder.features.homescreen.data.models.RecipeHomeModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GetRecipesRepositoryImpl @Inject constructor(private val openAiApi: OpenAiApi) :
    GetRecipesRepository {
    override fun getRecipes(query: String): Flow<List<RecipeHomeModel>> = callbackFlow {
        val messages = listOf(
            Message("system", "You are a helpful AI chef."),
            Message("user", "Give me recipes for: $query")
        )

        val request = OpenAiRequest(messages = messages)
        val call = openAiApi.getRecipes(request)

        val recipeImages = listOf(
            "https://images.pexels.com/photos/1633578/pexels-photo-1633578.jpeg",
            "https://images.pexels.com/photos/825661/pexels-photo-825661.jpeg",
            "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg",
            "https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg",
            "https://images.pexels.com/photos/3298182/pexels-photo-3298182.jpeg",
            "https://images.pexels.com/photos/3026802/pexels-photo-3026802.jpeg",
            "https://images.pexels.com/photos/376464/pexels-photo-376464.jpeg",
            "https://images.pexels.com/photos/1640772/pexels-photo-1640772.jpeg",
            "https://images.pexels.com/photos/1640775/pexels-photo-1640775.jpeg"
        )

        call.enqueue(object : Callback<OpenAIResponse> {
            override fun onResponse(
                call: Call<OpenAIResponse>,
                response: Response<OpenAIResponse>
            ) {
                if (response.isSuccessful) {
                    val contentBody = response.body()?.choices?.firstOrNull()?.message?.content
                    val recipe = contentBody?.substringBefore("Ingredients")
                    val ingredients =
                        contentBody?.substringAfter("Ingredients")?.substringBefore("Instructions")
                    val instructions = contentBody?.substringAfter("Instructions")
                    trySend(
                        listOf(
                            RecipeHomeModel(
                                title = recipe.toString(),
                                url = recipeImages.random(),
                                ingredients = ingredients,
                                instructions = instructions
                            )
                        )
                    )
                }
            }

            override fun onFailure(call: Call<OpenAIResponse>, t: Throwable) {
                close(t)
            }
        })
        awaitClose { call.cancel() }
    }
}