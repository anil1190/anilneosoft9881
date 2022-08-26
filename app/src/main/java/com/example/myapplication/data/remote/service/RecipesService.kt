package com.example.myapplication.data.remote.service

import com.example.myapplication.data.dto.RecipesItem
import retrofit2.Response
import retrofit2.http.GET

interface RecipesService {
    @GET("recipes.json")
    suspend fun fetchRecipes(): Response<List<RecipesItem>>
}