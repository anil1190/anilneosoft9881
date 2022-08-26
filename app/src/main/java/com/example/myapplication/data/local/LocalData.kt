package com.example.myapplication.data.local

import android.content.Context
import com.example.myapplication.data.PASS_WORD_ERROR
import com.example.myapplication.data.Resource
import com.example.myapplication.data.dto.LoginRequest
import com.example.myapplication.data.dto.LoginResponse
import javax.inject.Inject

class LocalData @Inject constructor(val context: Context) {

    fun doLogin(loginRequest: LoginRequest): Resource<LoginResponse> {
        if (loginRequest == LoginRequest("anil@gmail.com", "anil")) {
            return Resource.Success(
                LoginResponse("123", "Anil", "Singh",
                "FrunkfurterAlle", "77", "12000", "Berlin",
                "Germany", "anil@gmail.com")
            )
        }
        return Resource.DataError(PASS_WORD_ERROR)
    }
}