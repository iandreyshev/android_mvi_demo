package ru.iandreyshev.jokesfeed.presentation

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.iandreyshev.jokesfeed.domain.Filter
import ru.iandreyshev.jokesfeed.domain.Topic
import ru.iandreyshev.jokesfeed.domain.feed.GetFeedUseCase
import ru.iandreyshev.jokesfeed.domain.core.UseCaseResult
import ru.iandreyshev.jokesfeed.domain.feed.FilterItemsByQueryUseCase
import ru.iandreyshev.jokesfeed.system.mvi.Executor

class Executor(
    private val getFeed: GetFeedUseCase,
    private val filterItemsByQuery: FilterItemsByQueryUseCase
) : Executor<Action, Event, State, Result>() {

    private var mRefreshOperation: Job? = null

    override suspend fun execute(action: Action, getState: () -> State) = when (action) {
        Action.Init -> onInitFeed()
        is Action.QueryChanged -> onQueryChanged(getState, action.query)
        Action.OpenFilters -> onOpenFilters(getState)
        Action.CloseFilters -> reduce(Result.ChangeScreen(State.Screen.FEED))
        is Action.SelectTopics -> onTopicsChanged(action.topics)
        Action.ApplyFilters -> onApplyFilters(getState)
        Action.CancelRefresh -> onCancelRefresh()
    }

    private suspend fun onInitFeed() {
        when (val result = getFeed(filter = Filter.empty())) {
            is UseCaseResult.Success -> reduce(Result.FinishFirstLoading(feed = result.data))
            is UseCaseResult.Error -> {
                reduce(Result.FinishFirstLoading(feed = emptyList()))
                event(Event.ShowMessage("Ошибка загрузки данных"))
            }
        }
    }

    private fun onQueryChanged(getState: () -> State, newQuery: String) {
        when {
            newQuery.isBlank() -> reduce(Result.QueriedListChanged("", emptyList()))
            else -> {
                val filteredItems = filterItemsByQuery(getState().feedState.feed, query = newQuery)
                reduce(Result.QueriedListChanged(newQuery, filteredItems))
            }
        }
    }

    private fun onOpenFilters(getState: () -> State) {
        if (!getState().feedState.isRefreshing) {
            reduce(Result.ChangeScreen(State.Screen.FILTER))
        }
    }

    private fun onTopicsChanged(topics: Set<Topic>) {
        reduce(Result.ChangeTopics(topics))
    }

    private suspend fun onApplyFilters(getState: () -> State) {
        if (getState().filterState.draft == getState().filterState.current) {
            event(Event.ShowMessage("Фильтры не менялись"))
            return
        }

        reduce(Result.ChangeRefreshingState(isRefreshing = true))

        mRefreshOperation = coroutineScope?.launch {
            reduce(Result.ChangeScreen(State.Screen.FEED))

            when (val result = getFeed(filter = getState().filterState.draft)) {
                is UseCaseResult.Success -> {
                    reduce(Result.UpdateList(feed = result.data))
                    reduce(Result.ApplyFilters(getState().filterState.draft))
                }
                is UseCaseResult.Error -> {
                    reduce(Result.CancelRefresh)
                    event(Event.ShowMessage("Ошибка загрузки данных"))
                }
            }

            reduce(Result.ChangeRefreshingState(isRefreshing = false))
        }
    }

    private fun onCancelRefresh() {
        mRefreshOperation?.cancel()
        reduce(Result.CancelRefresh)
    }

}
