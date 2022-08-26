package com.example.myapplication.data

import com.example.myapplication.data.dto.LoginRequest
import com.example.myapplication.data.dto.LoginResponse
import com.example.myapplication.data.dto.Recipes
import com.example.myapplication.data.local.LocalData
import com.example.myapplication.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepository @Inject constructor(private val remoteData: RemoteData, private val localData: LocalData, private val ioDispatcher : CoroutineContext) : DataRepositorySource {
    override suspend fun requestRecipes(): Flow<Resource<Recipes>> {
        return flow {
            emit(remoteData.requestRecipes())
        }.flowOn(ioDispatcher)
    }

    override suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return flow {
             emit(localData.doLogin(loginRequest))
        }.flowOn(ioDispatcher)
    }
}