package com.example.recipefinder.features.homescreen.data.datasource

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApi {
    @POST("v1/chat/completions")
    fun getRecipes(@Body request: OpenAiRequest): Call<OpenAIResponse>
}