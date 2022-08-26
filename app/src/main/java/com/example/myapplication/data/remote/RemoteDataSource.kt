package com.example.myapplication.data.remote

import com.example.myapplication.data.Resource
import com.example.myapplication.data.dto.Recipes

internal interface RemoteDataSource{

suspend fun requestRecipes(): Resource<Recipes>
}