package com.example.myapplication.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object ImageData {

    var images = listOf(
        Image("https://media2.giphy.com/media/ZEBoAmoK5NVNso29xo/200.gif", 356, 200),
        Image("https://media3.giphy.com/media/W6XOLBwKcMyCeTtO3e/200.gif", 200, 200),
        Image("https://media0.giphy.com/media/3owypi68EvEuiasmDS/200.gif", 372, 200),
        Image("https://media4.giphy.com/media/QZybZmviJW0Yn9K1nn/200.gif", 356, 200),
        Image("https://media3.giphy.com/media/SXqnLsJpKhLU8eLjf1/200.gif", 200, 200),

        )
}

@Parcelize
data class Image(
    val url: String,
    val width: Int,
    val height: Int
) : Parcelable {
    val aspectRatio: Float get() = width.toFloat() / height.toFloat()
}