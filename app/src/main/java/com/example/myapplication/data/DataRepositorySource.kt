package com.example.myapplication.data

import com.example.myapplication.data.dto.LoginRequest
import com.example.myapplication.data.dto.LoginResponse
import com.example.myapplication.data.dto.Recipes
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun requestRecipes(): Flow<Resource<Recipes>>
    suspend fun doLogin(loginRequest : LoginRequest) : Flow<Resource<LoginResponse>>
}