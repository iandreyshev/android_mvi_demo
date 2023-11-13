package ru.iandreyshev.jokesfeed.domain.core

sealed interface ErrorType {
    object NoInternetConnection : ErrorType
}
