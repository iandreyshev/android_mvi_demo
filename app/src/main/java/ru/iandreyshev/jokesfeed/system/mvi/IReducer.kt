package ru.iandreyshev.jokesfeed.system.mvi

fun interface IReducer<TResult : Any, TState : Any> {
    fun TState.reduce(result: TResult): TState
}