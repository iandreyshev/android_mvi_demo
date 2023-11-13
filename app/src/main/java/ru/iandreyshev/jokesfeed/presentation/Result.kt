package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.domain.Filter
import ru.iandreyshev.jokesfeed.domain.Joke
import ru.iandreyshev.jokesfeed.domain.Topic

/**
 * Данные для модификации стейта в слое бизнес-логики
 * */
sealed interface Result {
    // Main
    data class ChangeScreen(val screen: State.Screen) : Result

    // Feed
    data class FinishFirstLoading(val feed: List<Joke>) : Result
    data class QueriedListChanged(val query: String, val list: List<Joke>) : Result
    data class UpdateList(val feed: List<Joke>) : Result
    object CancelFiltering : Result

    // Filters
    data class ChangeRefreshingState(val isRefreshing: Boolean) : Result
    data class SelectTopics(val topics: Set<Topic>) : Result
    data class ApplyFilters(val filter: Filter) : Result
}
