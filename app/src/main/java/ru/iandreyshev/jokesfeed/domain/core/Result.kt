package ru.iandreyshev.jokesfeed.domain.core

sealed class UseCaseResult<out T> {
    class Success<out T>(val data: T) : UseCaseResult<T>()
    class Error(val type: ErrorType) : UseCaseResult<Nothing>()
}
