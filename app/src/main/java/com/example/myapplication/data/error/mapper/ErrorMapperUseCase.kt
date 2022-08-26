package com.example.myapplication.data.error.mapper

interface ErrorMapperUseCase {
    fun getErrorString(errorId:Int): String
    val errorMap: Map<Int,String>
}