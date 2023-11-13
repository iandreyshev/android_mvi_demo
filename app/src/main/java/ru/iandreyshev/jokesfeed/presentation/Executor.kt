package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.domain.feed.FilterJokesByQueryUseCase
import ru.iandreyshev.jokesfeed.domain.feed.GetFeedUseCase
import ru.iandreyshev.jokesfeed.system.mvi.Executor

class Executor(
    private val getFeed: GetFeedUseCase,
    private val filterJokesByQuery: FilterJokesByQueryUseCase
) : Executor<Action, Event, State, Result>() {

    override suspend fun execute(action: Action, getState: () -> State) {
    }

}
