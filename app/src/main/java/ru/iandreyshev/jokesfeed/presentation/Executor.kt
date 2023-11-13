package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.system.mvi.Executor

class Executor : Executor<Action, Event, State, Result>() {

    override suspend fun execute(action: Action, getState: () -> State) = Unit

}
