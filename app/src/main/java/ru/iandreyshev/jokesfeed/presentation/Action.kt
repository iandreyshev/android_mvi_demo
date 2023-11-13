package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.domain.Topic

/**
 * Действия из слоя интерфейса (ввод пользователя)
 * */
sealed interface Action {
    // Feed
    object Init : Action
    class QueryChanged(val query: String) : Action
    object CancelFiltering : Action

    // Filters
    object OpenFilters : Action
    data class SelectTopics(val topics: Set<Topic>) : Action
    object ApplyFilters : Action
    object CloseFilters : Action
}
