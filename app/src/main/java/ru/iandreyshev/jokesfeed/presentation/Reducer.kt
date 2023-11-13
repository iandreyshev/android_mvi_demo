package ru.iandreyshev.jokesfeed.presentation

import ru.iandreyshev.jokesfeed.system.mvi.IReducer
import timber.log.Timber

class Reducer : IReducer<Result, State> {

    override fun State.reduce(result: Result): State = when (result) {
        is Result.FinishFirstLoading ->
            copy(
                feedState = feedState.copy(type = FeedState.Type.NORMAL, feed = result.feed)
            )

        is Result.QueriedListChanged ->
            copy(
                feedState = feedState.copy(query = result.query, queriedFeed = result.list)
            )

        is Result.UpdateList ->
            copy(
                feedState = feedState.copy(feed = result.feed)
            )

        is Result.ChangeScreen ->
            copy(screen = result.screen)

        is Result.ChangeTopics ->
            copy(
                filterState = filterState.copy(draft = filterState.draft.copy(topics = result.topics))
            )

        is Result.ApplyFilters ->
            copy(
                feedState = feedState.copy(query = ""),
                filterState = filterState.copy(current = filterState.draft)
            )

        is Result.ChangeRefreshingState ->
            copy(
                feedState = feedState.copy(isRefreshing = result.isRefreshing)
            )

        is Result.CancelRefresh -> {
            Timber.d("Cancel")
            Timber.d(filterState.current.toString())
            Timber.d(filterState.draft.toString())
            copy(
                feedState = feedState.copy(isRefreshing = false),
                filterState = filterState.copy(draft = filterState.current)
            )
        }
    }

}
