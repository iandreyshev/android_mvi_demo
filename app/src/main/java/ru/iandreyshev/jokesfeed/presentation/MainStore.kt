package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.data.FakeRemoteGateway
import ru.iandreyshev.jokesfeed.domain.feed.FilterJokesByQueryUseCase
import ru.iandreyshev.jokesfeed.domain.feed.GetJokesUseCase
import ru.iandreyshev.jokesfeed.system.mvi.Store
import ru.iandreyshev.jokesfeed.system.utils.CoroutineContextProvider

class MainStore : Store<State, Action, Event, Result>(
    executor = Executor(
        getFeed = GetJokesUseCase(
            gateway = FakeRemoteGateway(),
            contextProvider = CoroutineContextProvider.default()
        ),
        filterJokesByQuery = FilterJokesByQueryUseCase()
    ),
    reducer = Reducer(),
    initialState = State.default()
)
