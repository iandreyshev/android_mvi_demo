package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.domain.Topic

/**
 * Действия из слоя интерфейса (ввод пользователя)
 * */
sealed interface Action {
    // Feed
    object Init : Action
    data class QueryChanged(val query: String) : Action

    // Filters
    object OpenFilters : Action
    object CloseFilters : Action
    data class SelectTopics(val topics: Set<Topic>) : Action
    object ApplyFilters : Action
    object CancelFiltering : Action
}
