package ru.iandreyshev.jokesfeed.presentation

sealed interface Event {
    data class ShowMessage(val message: String) : Event
}
