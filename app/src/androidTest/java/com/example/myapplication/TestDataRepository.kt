package com.example.myapplication

import com.example.myapplication.TestUtils.dataStatus
import com.example.myapplication.TestUtils.initData
import com.example.myapplication.data.DataRepositorySource
import com.example.myapplication.data.NETWORK_ERROR
import com.example.myapplication.data.Resource
import com.example.myapplication.data.dto.LoginRequest
import com.example.myapplication.data.dto.LoginResponse
import com.example.myapplication.data.dto.Recipes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestDataRepository @Inject constructor() : DataRepositorySource {
    override suspend fun requestRecipes(): Flow<Resource<Recipes>> {
        return when (dataStatus) {
            DataStatus.Success -> {
                flow { emit(Resource.Success(initData())) }
            }
            DataStatus.Fail -> {
                flow { emit(Resource.DataError<Recipes>(errorCode = NETWORK_ERROR)) }
            }
            DataStatus.EmptyResponse -> {
                flow { emit(Resource.Success(Recipes(arrayListOf()))) }
            }
        }
    }

    override suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Success(LoginResponse("123", "Anil", "Singh",
                "FrunkfurterAlle", "77", "12000", "Berlin",
                "Germany", "anil@gmail.com")))
        }
    }
}