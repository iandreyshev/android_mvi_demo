package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.system.mvi.Store

class MainStore : Store<State, Action, Event, Result>(
    executor = Executor(),
    reducer = Reducer(),
    initialState = State.default()
)
