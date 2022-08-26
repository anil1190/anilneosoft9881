package com.example.myapplication

sealed class DataStatus{
    object Success : DataStatus()
    object Fail : DataStatus()
    object EmptyResponse : DataStatus()
}
