package com.example.myapplication.usecase

import com.example.myapplication.data.Error
import com.example.myapplication.data.error.mapper.ErrorMapper
import javax.inject.Inject

class ErrorManager @Inject constructor(private val errorMapper : ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorMap.getValue(errorCode))
    }


}