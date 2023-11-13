package ru.iandreyshev.jokesfeed.domain.feed

import ru.iandreyshev.jokesfeed.domain.Joke
import ru.iandreyshev.jokesfeed.domain.Filter

interface Gateway {
    suspend fun get(filter: Filter): List<Joke>
}
