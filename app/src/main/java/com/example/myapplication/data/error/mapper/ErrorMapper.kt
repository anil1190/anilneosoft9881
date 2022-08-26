package com.example.myapplication.data.error.mapper

import android.content.Context
import com.example.myapplication.R
import com.example.myapplication.data.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorMapper @Inject constructor(@ApplicationContext val context: Context) : ErrorMapperUseCase {

    override fun getErrorString(errorId: Int): String {
         return context.getString(errorId)
    }

    override val errorMap: Map<Int, String>
        get() = mapOf(
            Pair(NO_INTERNET_CONNECTION, getErrorString(R.string.no_internet)),
            Pair(NETWORK_ERROR,getErrorString(R.string.network_error)),
            Pair(PASS_WORD_ERROR,getErrorString(R.string.invalid_password)),
            Pair(USER_NAME_ERROR,getErrorString(R.string.invalid_user_name)),
            Pair(CHECK_YOUR_FIELDS,getErrorString(R.string.invalid_user_and_password)),
            Pair(SEARCH_ERROR,getErrorString(R.string.invalid_search_item))

        ).withDefault {getErrorString(R.string.network_error)}


}