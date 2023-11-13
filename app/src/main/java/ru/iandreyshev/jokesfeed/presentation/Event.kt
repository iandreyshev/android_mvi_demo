package ru.iandreyshev.jokesfeed.presentation

/**
 * Одноразовые события из слоя логики в интерфейс
 * */
sealed interface Event {
    data class ShowMessage(val message: String) : Event
}
