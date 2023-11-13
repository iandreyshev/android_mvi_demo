package ru.iandreyshev.jokesfeed.system.utils

fun <T> uiLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
