package com.example.recipefinder.features.homescreen.data.datasource

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {
    // Need to access the apiKey by using BuildConfig
    // We need to store it in local properties and to enable buildConfig
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("v1/chat/completions")
    fun getRecipes(@Body request: OpenAiRequest): Call<OpenAIResponse>
}