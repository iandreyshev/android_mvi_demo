package ru.iandreyshev.jokesfeed.domain.core

sealed interface ErrorType

object Unknown : ErrorType
object NoInternetConnection : ErrorType
