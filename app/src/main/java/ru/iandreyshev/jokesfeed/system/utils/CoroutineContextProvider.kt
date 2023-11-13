package ru.iandreyshev.jokesfeed.system.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

data class CoroutineContextProvider(
    val main: CoroutineContext,
    val io: CoroutineContext,
    val default: CoroutineContext
) {

    companion object {
        fun default(
            main: CoroutineDispatcher = Dispatchers.Main,
            io: CoroutineDispatcher = Dispatchers.IO,
            default: CoroutineDispatcher = Dispatchers.Default,
        ) = CoroutineContextProvider(
            main = main,
            io = io,
            default = default
        )
    }

}
