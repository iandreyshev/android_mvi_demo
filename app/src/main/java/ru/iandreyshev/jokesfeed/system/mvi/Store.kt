package ru.iandreyshev.jokesfeed.system.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.iandreyshev.jokesfeed.system.utils.uiLazy

abstract class Store<TState : Any, TAction : Any, TEvent : Any, TResult : Any>(
    private val executor: Executor<TAction, TEvent, TState, TResult>,
    private val reducer: IReducer<TResult, TState>,
    initialState: TState
) : ViewModel() {

    val state: StateFlow<TState> by uiLazy { mState.asStateFlow() }
    val event: SharedFlow<TEvent> by uiLazy { mEvent.asSharedFlow() }

    private val mState = MutableStateFlow(initialState)
    private val mEvent = MutableSharedFlow<TEvent>()

    init {
        executor.init(
                event = ::event,
                reduce = {
                    viewModelScope.launch {
                        reduce(it)
                    }
                },
                coroutineScope = viewModelScope,
                reduceSuspend = ::reduce
        )
    }

    fun accept(action: TAction) {
        viewModelScope.launch {
            executor.execute(action, ::getState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        executor.dispose()
    }

    private fun event(event: TEvent) {
        viewModelScope.launch {
            mEvent.emit(event)
        }
    }

    private fun getState(): TState = state.value

    private fun reduce(result: TResult) {
        reducer.run { mState.value = getState().reduce(result) }
    }

}
