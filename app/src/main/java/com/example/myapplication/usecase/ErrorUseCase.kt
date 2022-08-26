package com.example.myapplication.usecase

import com.example.myapplication.data.Error

interface ErrorUseCase {
    fun getError(errorCode:Int) : Error
}