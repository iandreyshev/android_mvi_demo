package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.domain.feed.FilterJokesByQueryUseCase
import ru.iandreyshev.jokesfeed.domain.feed.GetJokesUseCase
import ru.iandreyshev.jokesfeed.system.mvi.Executor

class Executor(
    private val getFeed: GetJokesUseCase,
    private val filterJokesByQuery: FilterJokesByQueryUseCase
) : Executor<Action, Event, State, Result>() {

    override suspend fun execute(action: Action, getState: () -> State) {
    }

}
