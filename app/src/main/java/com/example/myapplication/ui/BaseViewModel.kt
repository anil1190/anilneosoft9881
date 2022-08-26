package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import com.example.myapplication.usecase.ErrorManager
import javax.inject.Inject

abstract class BaseViewModel : ViewModel(){

    @Inject
    lateinit var errorManager : ErrorManager
}