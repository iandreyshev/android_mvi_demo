package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.domain.Topic

sealed interface Action {
    // Feed
    object Init : Action
    class QueryChanged(val query: String) : Action
    object CancelRefresh : Action

    // Filters
    object OpenFilters : Action
    data class SelectTopics(val topics: Set<Topic>) : Action
    object ApplyFilters : Action
    object CloseFilters : Action
}
