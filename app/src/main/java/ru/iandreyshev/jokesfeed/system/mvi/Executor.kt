package ru.iandreyshev.jokesfeed.system.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class Executor<TAction : Any, TEvent : Any, TState : Any, TResult : Any> {

    protected val event get() = mEvent
    protected val reduce get() = mReduce
    protected val coroutineScope get() = mCoroutineScope
    protected val reduceSuspend get() = mReduceSuspend

    private var mEvent: (TEvent) -> Unit = {}
    private var mReduce: (TResult) -> Unit = {}
    private var mReduceSuspend: suspend (TResult) -> Unit = {}
    private var mCoroutineScope: CoroutineScope? = null

    fun init(
            event: (TEvent) -> Unit,
            reduce: (TResult) -> Unit,
            reduceSuspend: suspend (TResult) -> Unit,
            coroutineScope: CoroutineScope
    ) {
        this.mEvent = event
        this.mReduce = reduce
        this.mReduceSuspend = reduceSuspend
        this.mCoroutineScope = coroutineScope
        onInit()
    }

    fun dispose() {
        onDispose()
        mEvent = {}
        mReduce = {}
        mReduceSuspend = {}
        mCoroutineScope?.cancel()
        mCoroutineScope = null
        onDispose()
    }

    abstract suspend fun execute(action: TAction, getState: () -> TState)

    protected open fun onInit() = Unit
    protected open fun onDispose() = Unit

}