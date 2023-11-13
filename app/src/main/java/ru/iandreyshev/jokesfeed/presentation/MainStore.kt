package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.data.FakeRemoteFeedGateway
import ru.iandreyshev.jokesfeed.domain.feed.FilterItemsByQueryUseCase
import ru.iandreyshev.jokesfeed.domain.feed.GetFeedUseCase
import ru.iandreyshev.jokesfeed.system.mvi.Store
import ru.iandreyshev.jokesfeed.system.utils.CoroutineContextProvider

class MainStore : Store<State, Action, Event, Result>(
    executor = Executor(
        getFeed = GetFeedUseCase(
            gateway = FakeRemoteFeedGateway(),
            contextProvider = CoroutineContextProvider.default()
        ),
        filterItemsByQuery = FilterItemsByQueryUseCase()
    ),
    reducer = Reducer(),
    initialState = State.default()
)
