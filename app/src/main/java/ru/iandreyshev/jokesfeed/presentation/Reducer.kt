package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.system.mvi.IReducer

class Reducer : IReducer<Result, State> {

    override fun State.reduce(result: Result): State = this

}
