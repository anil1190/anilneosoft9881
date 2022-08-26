package com.example.myapplication.utils

object EspressoIdLingResource{

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = SimpleCountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    // Espresso does not work well with coroutines yet. See
    // https://github.com/Kotlin/kotlinx.coroutines/issues/982
     EspressoIdLingResource.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdLingResource.decrement() // Set app as idle.
    }
}