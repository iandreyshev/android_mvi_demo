package ru.iandreyshev.jokesfeed.presentation

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.iandreyshev.jokesfeed.domain.Filter
import ru.iandreyshev.jokesfeed.domain.Topic
import ru.iandreyshev.jokesfeed.domain.feed.GetJokesUseCase
import ru.iandreyshev.jokesfeed.domain.core.UseCaseResult
import ru.iandreyshev.jokesfeed.domain.feed.FilterJokesByQueryUseCase
import ru.iandreyshev.jokesfeed.system.mvi.Executor

class Executor(
    private val getFeed: GetJokesUseCase,
    private val filterJokesByQuery: FilterJokesByQueryUseCase
) : Executor<Action, Event, State, Result>() {

    private var mFilteringJob: Job? = null

    override suspend fun execute(action: Action, getState: () -> State) = when (action) {
        Action.Init -> onInitFeed()
        is Action.QueryChanged -> onQueryChanged(getState, action.query)
        Action.OpenFilters -> onOpenFilters(getState)
        Action.CloseFilters -> reduce(Result.ChangeScreen(State.Screen.FEED))
        is Action.SelectTopics -> onTopicsSelected(action.topics)
        Action.ApplyFilters -> onApplyFilters(getState)
        Action.CancelFiltering -> onCancelFiltering()
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

    private fun onQueryChanged(getState: () -> State, query: String) {
        when {
            query.isBlank() -> reduce(Result.QueriedListChanged("", emptyList()))
            else -> {
                val filtered = filterJokesByQuery(getState().feedState.jokes, query = query)
                reduce(Result.QueriedListChanged(query, filtered))
            }
        }
    }

    private fun onOpenFilters(getState: () -> State) {
        if (!getState().feedState.isFilteringInProgress) {
            reduce(Result.ChangeScreen(State.Screen.FILTER))
        }
    }

    private fun onTopicsSelected(topics: Set<Topic>) {
        reduce(Result.SelectTopics(topics))
    }

    private suspend fun onApplyFilters(getState: () -> State) {
        if (getState().filterState.draft == getState().filterState.current) {
            event(Event.ShowMessage("Фильтры не менялись"))
            return
        }

        reduce(Result.ChangeRefreshingState(isRefreshing = true))

        mFilteringJob = coroutineScope?.launch {
            reduce(Result.ChangeScreen(State.Screen.FEED))

            when (val result = getFeed(filter = getState().filterState.draft)) {
                is UseCaseResult.Success -> {
                    reduce(Result.UpdateList(feed = result.data))
                    reduce(Result.ApplyFilters(getState().filterState.draft))
                }
                is UseCaseResult.Error -> {
                    reduce(Result.CancelFiltering)
                    event(Event.ShowMessage("Ошибка загрузки данных"))
                }
            }

            reduce(Result.ChangeRefreshingState(isRefreshing = false))
        }
    }

    private fun onCancelFiltering() {
        mFilteringJob?.cancel()
        reduce(Result.CancelFiltering)
    }

}
