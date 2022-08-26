package com.example.myapplication.di

import com.example.myapplication.data.error.mapper.ErrorMapper
import com.example.myapplication.data.error.mapper.ErrorMapperUseCase
import com.example.myapplication.usecase.ErrorManager
import com.example.myapplication.usecase.ErrorUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {
    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorUseCase

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperUseCase
}