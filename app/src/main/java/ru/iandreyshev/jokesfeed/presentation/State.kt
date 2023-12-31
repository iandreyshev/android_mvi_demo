package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.domain.Joke
import ru.iandreyshev.jokesfeed.domain.Filter

data class State(
    val screen: Screen,
    val feedState: FeedState,
    val filterState: FilterState
) {

    enum class Screen {
        FEED,
        FILTER,
    }

    companion object {
        fun default() = State(
            screen = Screen.FEED,
            feedState = FeedState(
                type = FeedState.Type.FIRST_LOADING,
                jokes = emptyList(),
                query = "",
                filteredByQueryJokes = emptyList(),
                filter = Filter.empty(),
                isFilteringInProgress = false,
            ),
            filterState = FilterState(
                current = Filter.empty(),
                draft = Filter.empty()
            )
        )
    }

}

data class FeedState(
    val type: Type,
    val jokes: List<Joke>,
    val query: String,
    val filteredByQueryJokes: List<Joke>,
    val filter: Filter,
    val isFilteringInProgress: Boolean
) {
    enum class Type {
        FIRST_LOADING,
        NORMAL;
    }
}

data class FilterState(
    val current: Filter,
    val draft: Filter
)