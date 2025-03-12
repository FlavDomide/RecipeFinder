package com.example.recipefinder.di

import com.example.recipefinder.BuildConfig
import com.example.recipefinder.features.homescreen.data.datasource.OpenAiApi
import com.example.recipefinder.features.homescreen.data.datasource.RetrofitInterceptor
import com.example.recipefinder.features.homescreen.data.repositories.GetRecipesRepository
import com.example.recipefinder.features.homescreen.data.repositories.GetRecipesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://api.openai.com/"
    private const val apiKey = BuildConfig.API_KEY
    private val client = OkHttpClient.Builder()
        .addInterceptor(RetrofitInterceptor(apiKey))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOpenAIApi(retrofit: Retrofit): OpenAiApi = retrofit.create(OpenAiApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(openAiApi: OpenAiApi): GetRecipesRepository =
        GetRecipesRepositoryImpl(openAiApi)
}